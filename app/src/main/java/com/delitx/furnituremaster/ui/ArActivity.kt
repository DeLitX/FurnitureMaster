package com.delitx.furnituremaster.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data.network.ServerRequestsImpl
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.*
import java.io.File

class ArActivity : AppCompatActivity() {
    lateinit var mArFragment: ArFragment
    private var mSession: Session? = null
    private var mObject: TransformableNode? = null
    lateinit var mModelRenderable: ModelRenderable

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        mArFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        mArFragment.setOnSessionInitializationListener {
            mSession = it
        }
        startAr()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun buildModel(file: File) {
        val renderableSource = RenderableSource.builder()
            .setSource(this, Uri.parse(file.path), RenderableSource.SourceType.GLB)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()
        ModelRenderable.builder().setSource(this, renderableSource).setRegistryId(file.path).build()
            .thenAccept { modelRenderable ->
                mModelRenderable = modelRenderable
            }
    }

    private val CAMERA_PERMISSION_CODE = 0
    private val CAMERA_PERMISSION: String = Manifest.permission.CAMERA
    var mUserRequestedInstall = true

    fun hasCameraPermission(): Boolean {
        return (
            ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION)
                == PackageManager.PERMISSION_GRANTED
            )
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(CAMERA_PERMISSION),
            CAMERA_PERMISSION_CODE
        )
    }

    fun shouldShowRequestPermissionRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION)
    }

    fun launchPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", this.packageName, null)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startAr() {
        if (!hasCameraPermission()) {
            requestCameraPermission()
            return
        }
        val values = navArgs<ArActivityArgs>()
        val file = File.createTempFile("000" + values.value.id.toString(), ".glb")
        ServerRequestsImpl().downloadFile(values.value.link, file) {
            buildModel(file)

            mArFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
                if (mObject == null) {
                    val ancorNode = AnchorNode(hitResult.createAnchor())
                    val obj = TransformableNode(mArFragment.transformationSystem)
                    obj.renderable = mModelRenderable
                    obj.scaleController.minScale = 0.1f
                    obj.scaleController.maxScale = 1f
                    obj.rotationController
                    obj.setParent(ancorNode)
                    mArFragment.arSceneView.scene.addChild(ancorNode)
                    mObject = obj
                    obj.select()
                } else {
                    mObject!!.select()
                }
            }
        }
    }

    override fun onDestroy() {
        mSession?.close()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasCameraPermission()) {
            Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_LONG)
                .show()
            if (shouldShowRequestPermissionRationale()) {
                launchPermissionSettings()
            }
        }
    }
}
