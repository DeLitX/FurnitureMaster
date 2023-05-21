package com.delitx.furnituremaster.data_models.utils

import com.delitx.furnituremaster.data_models.ProductProperty
import com.delitx.furnituremaster.data_models.PropertyValue
import com.delitx.furnituremaster.exceptions.WrongDataException

fun getValuesByPropertyId(
    map: Map<ProductProperty, List<PropertyValue>>,
    id: Int
): List<PropertyValue> {
    val keys = map.keys
    return map[keys.find { it.id == id }] ?: throw WrongDataException()
}