package com.delitx.furnituremaster.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommentedImage(
    @PrimaryKey
    val id: Int,
    val link: String,
    val comment: MultiLanguageString
)
