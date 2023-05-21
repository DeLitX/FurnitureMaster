package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delitx.furnituremaster.data_models.MultiLanguageString
import com.delitx.furnituremaster.data_models.ProductProperty

@Entity(tableName = "product_property")
data class ProductPropertyDTO(
    @PrimaryKey
    val id: Int,
    var name: MultiLanguageString,
    var notShowInFilter: Boolean = false
) {
    fun toModel(): ProductProperty {
        return ProductProperty(id = id, name = name, notShowInFilter = notShowInFilter)
    }
}
