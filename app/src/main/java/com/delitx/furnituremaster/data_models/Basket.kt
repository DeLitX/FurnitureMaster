package com.delitx.furnituremaster.data_models

data class Basket(
    var id: Int = 0,
    var phoneNumber: String = "",
    var name: String = "",
    var comment: String = "",
    var products: List<Product> = listOf()
)
