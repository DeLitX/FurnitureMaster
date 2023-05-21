package com.delitx.furnituremaster.data

import androidx.lifecycle.LiveData
import com.delitx.furnituremaster.data.network.DataLoadState
import com.delitx.furnituremaster.data_models.NamedValue
import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.dtos.ProductDTO

interface ProductsRepository {
    val isDataUpdated: LiveData<Boolean>
    val errorStates: LiveData<ErrorStates>
    val placeSameOrderState: LiveData<DataLoadState<Boolean>>
    fun resetPlaceSameOrderState()
    fun getLiveProductByName(productId: Int): LiveData<ProductDTO?>
    suspend fun getProductsByIds(products: List<Int>): List<Product>
    suspend fun getProductsByValueIds(ids: List<Int>): List<Product>
    suspend fun productDtoToProduct(products: List<ProductDTO>): List<Product>
    fun getAllProducts(): LiveData<List<Product>>
    fun getCollection(id: Int): LiveData<NamedValue>
}

sealed class ErrorStates() {
    object AllRight : ErrorStates()
    object VersionIncorrect : ErrorStates()
    object InternetConnectionError : ErrorStates()
}
