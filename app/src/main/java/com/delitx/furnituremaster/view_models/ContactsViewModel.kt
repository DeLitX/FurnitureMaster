package com.delitx.furnituremaster.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.delitx.furnituremaster.data.network.ServerRequestsImpl
import com.delitx.furnituremaster.data_models.MapMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    val markers = MutableLiveData<List<MapMarker>>(listOf())
    init {
        getMarkers()
    }
    private fun getMarkers() {
        viewModelScope.launch {
            withContext(IO) {
                val mFirebaseRequests = ServerRequestsImpl()
                val list = mFirebaseRequests.getMapMarkers()
                markers.postValue(list)
            }
        }
    }
}
