package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.MapMarker
import com.delitx.furnituremaster.data_models.MultiLanguageString

data class MapMarkerNetworkDTO(val id: Int, val latitude: Double, val longitude: Double, val comment: Map<String, String>) {
    fun toModel(): MapMarker {
        return MapMarker(id, latitude, longitude, MultiLanguageString(comment))
    }
}
