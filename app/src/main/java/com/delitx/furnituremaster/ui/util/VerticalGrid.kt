package com.delitx.furnituremaster.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.min

@Composable
fun <T> VerticalGrid(
    colNumber: Int,
    items: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (item: T) -> Unit
) {
    val rowsNumber: Int = items.size / colNumber + if (items.size % colNumber != 0) 1 else 0
    for (i in 0 until rowsNumber) {
        Row(modifier = modifier) {
            for (t in 0 until min(colNumber, items.size - i * colNumber)) {
                Box(modifier = Modifier.fillMaxWidth(1f / (colNumber - t))) {
                    content(items[i * colNumber + t])
                }
            }
        }
    }
}
