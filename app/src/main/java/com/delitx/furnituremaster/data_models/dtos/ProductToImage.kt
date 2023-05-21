package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity

@Entity(primaryKeys = ["productId", "imageId"])
data class ProductToImage(val productId: Int, val imageId: Int)
