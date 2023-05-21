package com.delitx.furnituremaster.data_models

data class ProductPropertiesSet(val set: MutableMap<Int, PropertyValue>) {
    override fun toString(): String {
        var result: String = "{"
        var isFirst = true
        for (i in set) {
            if (!isFirst) {
                result += ','
            }
            result += "[${i.key}]:[${i.value}]"
            isFirst = false
        }
        return "$result}"
    }
}
