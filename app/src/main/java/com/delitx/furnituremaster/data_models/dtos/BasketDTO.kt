package com.delitx.furnituremaster.data_models.dtos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket")
data class BasketDTO(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var phoneNumber: String = "",
    var name: String = "",
    var comment: String = ""
)
