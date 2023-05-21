package com.delitx.furnituremaster.data.local.daos.utils

import com.delitx.furnituremaster.data.local.AppDB

fun <T, R> performBulkAction(
    items: List<T>,
    action: (List<T>) -> List<R>,
    tempListSize: Int = AppDB.MAX_LIST_SIZE
): List<R> {
    val result = mutableListOf<R>()
    val itemsChunked = items.chunked(tempListSize)
    for (i in itemsChunked) {
        result.addAll(action(i))
    }
    return result
}

suspend fun <T, R> performBulkActionSuspend(
    items: List<T>,
    action: suspend (List<T>) -> List<R>,
    tempListSize: Int = AppDB.MAX_LIST_SIZE
): List<R> {
    val result = mutableListOf<R>()
    val itemsChunked = items.chunked(tempListSize)
    for (i in itemsChunked) {
        result.addAll(action(i))
    }
    return result
}
