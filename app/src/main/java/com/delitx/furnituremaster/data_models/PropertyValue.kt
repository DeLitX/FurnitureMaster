package com.delitx.furnituremaster.data_models

abstract class PropertyValue() {
    abstract var id: Int
    abstract var value: Int
    abstract fun getPropertyName(): String
}
