package com.delitx.furnituremaster.data.network

import android.net.Uri
import com.delitx.furnituremaster.data_models.CommentedImage
import com.delitx.furnituremaster.data_models.MapMarker
import com.delitx.furnituremaster.data_models.PropertyValue
import com.delitx.furnituremaster.data_models.dtos.ProductDTO
import com.delitx.furnituremaster.data_models.dtos.ProductPropertyDTO
import java.io.File

interface ServerRequests {
    fun downloadFileLink(link: String, file: File, onSuccess: (link: Uri) -> Unit)
    fun downloadFile(link: String, file: File, onSuccess: () -> Unit)
    suspend fun getMapMarkers(): List<MapMarker>
    suspend fun getAllProducts(): List<ProductDTO>
    suspend fun getAllProperties(): List<ProductPropertyDTO>
    suspend fun getAllVariations(): List<PropertyValue>
    suspend fun getAllImages(): List<CommentedImage>
    suspend fun markProductAddedToBasket(productId: String)

    fun getTimestamp(): Long
    fun getAndroidVersion(): Long
}
