package com.delitx.furnituremaster.utils

fun <T> List<T>.truncate(): List<T> {
    return toSet().toList()
}
