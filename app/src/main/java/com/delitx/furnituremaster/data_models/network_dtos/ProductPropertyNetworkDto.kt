package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.MultiLanguageString
import com.delitx.furnituremaster.data_models.dtos.ProductPropertyDTO
import com.google.gson.annotations.SerializedName

class ProductPropertyNetworkDto(
    val id: Int,
    var name: Map<String, String>,
    @SerializedName("not_show_in_filter")
    var notShowInFilter: Boolean = false
) {
    fun toProductPropertyDTO(): ProductPropertyDTO {
        return ProductPropertyDTO(id, MultiLanguageString(name), notShowInFilter)
    }
}
