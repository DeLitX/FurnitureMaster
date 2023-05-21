package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity
import com.delitx.furnituremaster.data_models.ProductPropertiesSet

@Entity(tableName = "product_configuration", primaryKeys = ["productId", "basketId", "chosenConfiguration"])
data class ProductConfiguration(
    val productId: Int,
    val basketId: Int,
    val chosenConfiguration: ProductPropertiesSet,
    var amount: Int = 1
)
