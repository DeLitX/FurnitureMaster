package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.delitx.furnituremaster.data_models.MultiLanguageString

@Entity(tableName = "product")
data class ProductDTO(
    @PrimaryKey
    val id: Int,
    var name: MultiLanguageString,
    var priceFormula: String,
    var order: List<Int>,
    var modelPath: String? = null,
    // @Ignore
    val imageUrl: List<Int>,
    @Ignore
    var propertiesList: Map<Int, List<Int>>
) {
    constructor(
        id: Int,
        name: MultiLanguageString,
        priceFormula: String,
        order: List<Int>,
        modelPath: String?,
        imageUrl: List<Int>,
    ) : this(id, name, priceFormula, order, modelPath, imageUrl, mapOf())
}
