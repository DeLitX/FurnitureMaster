package com.delitx.furnituremaster.data_models.network_dtos

import com.delitx.furnituremaster.data_models.MultiLanguageString
import com.delitx.furnituremaster.data_models.dtos.ProductDTO
import com.google.gson.annotations.SerializedName

class ProductNetworkDto(
    val id: Int,
    var name: Map<String, String>,
    @SerializedName("price_formula")
    var priceFormula: String,
    @SerializedName("images")
    val imageUrl: List<Int>,
    @SerializedName("properties_list")
    var propertiesList: List<PropertyToVariationsNetworkDto>,
    @SerializedName("model_path")
    var modelPath: String? = null
) {
    fun toProductDto(): ProductDTO {
        val map = mutableMapOf<Int, List<Int>>()
        val order = mutableListOf<Int>()
        for (i in propertiesList) {
            map[i.id] = i.variations
            order.add(i.id)
        }
        return ProductDTO(
            id = id,
            name = MultiLanguageString(name),
            priceFormula = priceFormula,
            order = order,
            imageUrl = imageUrl,
            propertiesList = map,
            modelPath = modelPath
        )
    }
}
