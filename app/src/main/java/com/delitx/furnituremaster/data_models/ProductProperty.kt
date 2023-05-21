package com.delitx.furnituremaster.data_models

data class ProductProperty(
    val id: Int,
    var name: MultiLanguageString,
    var notShowInFilter: Boolean = false
)
