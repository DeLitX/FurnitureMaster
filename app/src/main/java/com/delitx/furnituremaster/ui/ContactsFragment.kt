package com.delitx.furnituremaster.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.MapMarker
import com.delitx.furnituremaster.ui.adapters.ContactsAdapter
import com.delitx.furnituremaster.view_models.ContactsViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {
    private lateinit var v: View
    private var mMapView: MapView? = null
    private var mGMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private val ERROR_DIALOG_REQUEST = 9001
    private val REQUEST_MAP_PERMISSIONS = 1234
    private var mLocationPermissionGranted: Boolean = false
    private val PERMISSION_REQUEST_ENABLE_GPS = 12312
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mIsGpsDialogShowing: Boolean = false
    private val MAX_ZOOM: Float = 30f
    private val MIN_ZOOM: Float = (-20).toFloat()
    private var isFirstEnter: Boolean = true
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = ContactsAdapter(object : ContactsAdapter.ClickListenerInterface {
        override fun onClick(marker: MapMarker) {
            moveCamera(LatLng(marker.latitude, marker.longitude))
        }

        override fun onDirectionClick(marker: MapMarker) {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${marker.latitude},${marker.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            }
        }
    })
    private var markerList = mutableStateOf(listOf<MapMarker>())
    val viewModel: ContactsViewModel by viewModels()
    private val markersToAdd = mutableListOf<MapMarker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.contacts_fragment, container, false)
        mMapView = v.findViewById(R.id.map_view)
        val mapBundle: Bundle? = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        mMapView?.onCreate(mapBundle)
        askLocationPermission()
        if (isServicesOK() && isGPSEnabled() && mLocationPermissionGranted) {
            setupMap()
        }
        mRecyclerView = v.findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager =
            LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = mAdapter
        viewModel.markers.observe(viewLifecycleOwner) {
            markerList.value = it
            mAdapter.submitList(it)
            for (i in it) {
                addMarker(i)
            }
        }
        return v
    }

    private fun setupMap() {
        mMapView?.getMapAsync { it ->
            mGMap = it
            val uiSettings = mGMap!!.uiSettings
            uiSettings.isCompassEnabled = true
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true

            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@getMapAsync
            }
            mGMap!!.setMinZoomPreference(MIN_ZOOM)
            mGMap!!.setMaxZoomPreference(MAX_ZOOM)
            for (i in markersToAdd) {
                addMarker(i)
            }
        }
        getDeviceLocation()
    }

    private fun addMarker(marker: MapMarker) {
        if (mGMap != null) {
            mGMap!!.addMarker(MarkerOptions().position(LatLng(marker.latitude, marker.longitude)))
        } else {
            markersToAdd.add(marker)
        }
    }

    private fun moveCamera(position: LatLng, zoom: Float = 12f, tilt: Float = 20f) {
        val tiltTemp = if (tilt <= 0) 0f else if (tilt > 90) 90f else tilt
        val camBuilder = CameraPosition.builder()
        camBuilder.zoom(12f)
        camBuilder.tilt(20f)
        camBuilder.target(position)
        val cameraPosition = camBuilder.build()
        mGMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun moveCamera(position: Pair<Double, Double>, zoom: Float = 12f, tilt: Float = 20f) {
        moveCamera(LatLng(position.first, position.second), zoom, tilt)
    }

    private fun getDeviceLocation() {
        this.context?.let { it ->
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it)
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (isFirstEnter && location != null) {
                    moveCamera(LatLng(location.latitude, location.longitude))
                    isFirstEnter = !isFirstEnter
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        val manager: LocationManager =
            this.activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun isServicesOK(): Boolean {
        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.context)
        if (available == ConnectionResult.SUCCESS) {
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this.activity, available, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this.context, "You can't make map requests", Toast.LENGTH_LONG).show()
        }
        return false
    }

    private fun askLocationPermission() {
        if (this.activity?.applicationContext?.let {
            ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
        } == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_MAP_PERMISSIONS
                )
            }
        }
    }

    private fun buildAlertMessageNoGps() {
        if (!mIsGpsDialogShowing) {
            val builder = AlertDialog.Builder(this.context)
            builder.setMessage(R.string.gps_require)
                .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, PERMISSION_REQUEST_ENABLE_GPS)
                }
                .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
                .setOnCancelListener {
                    this.activity?.onBackPressed()
                    mIsGpsDialogShowing = false
                }
            val alert = builder.create()
            mIsGpsDialogShowing = true
            alert.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_MAP_PERMISSIONS) {
            if (grantResults.isNotEmpty()) {
                var counter = 0
                for (i in 0..permissions.size - 1) {
                    val permission = permissions[i]
                    if (Manifest.permission.ACCESS_FINE_LOCATION == permission) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true
                            return
                        }
                    }
                }
                mLocationPermissionGranted = false
                askLocationPermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_ENABLE_GPS) {
            mIsGpsDialogShowing = false
            if (mLocationPermissionGranted) {
                setupMap()
            } else {
                askLocationPermission()
            }
        }
    }

    override fun onResume() {
        mMapView?.onStart()
        super.onResume()
        askLocationPermission()
        if (isGPSEnabled() && isServicesOK() && mLocationPermissionGranted) {
            setupMap()
        }
    }

    override fun onStart() {
        mMapView?.onStart()
        super.onStart()
    }

    override fun onStop() {
        mMapView?.onStop()
        super.onStop()
        isFirstEnter = true
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mMapView?.onLowMemory()
        super.onLowMemory()
    }
}
