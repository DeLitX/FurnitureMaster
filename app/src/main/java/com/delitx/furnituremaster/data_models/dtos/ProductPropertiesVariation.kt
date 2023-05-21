package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity

@Entity(primaryKeys = ["productId", "propertyId", "valueId"])
data class ProductPropertiesVariation(val productId: Int, val propertyId: Int, val valueId: Int)
