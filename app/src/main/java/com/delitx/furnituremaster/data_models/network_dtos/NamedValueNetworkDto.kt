package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.MultiLanguageString
import com.delitx.furnituremaster.data_models.NamedValue
import com.google.gson.annotations.SerializedName

data class NamedValueNetworkDto(
    var id: Int,
    var name: Map<String, String>,
    var value: Int,
    var image: String? = "",
    @SerializedName("catalogue_images")
    var catalogueImages: List<String>?
) {
    fun toModel(): NamedValue {
        return NamedValue(
            id = id,
            name = MultiLanguageString(name),
            value = value,
            image = image ?: "",
            catalogueImages = catalogueImages ?: listOf()
        )
    }
}
