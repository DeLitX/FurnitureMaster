package com.delitx.furnituremaster.data

import androidx.lifecycle.LiveData
import com.delitx.furnituremaster.data_models.Basket
import com.delitx.furnituremaster.data_models.dtos.BasketDTO
import com.delitx.furnituremaster.data_models.dtos.ProductConfiguration

interface BasketsRepository {
    fun getCurrentBasketLive(): LiveData<BasketDTO>
    fun getBasketList(): LiveData<List<BasketDTO>>
    fun saveCurrentBasketId(id: Int)
    suspend fun saveNewCurrentBasketId(id: Int)
    fun getSavedCurrentBasketId(): Int
    fun checkoutBasket(): Boolean
    fun getById(id: Int): LiveData<BasketDTO>
    fun getConfigurationsByBasketLive(basketId: Int): LiveData<List<ProductConfiguration>>
    suspend fun getCurrentBasket(): BasketDTO
    suspend fun initiateCurrentBasket()
    suspend fun saveBasket(basket: BasketDTO)
    suspend fun updateBasket(basket: BasketDTO)
    suspend fun deleteBasket(basket: BasketDTO)
    suspend fun convertBasketDtoToBasket(dto: BasketDTO, repository: ProductsRepository): Basket
    suspend fun getConfigurationsByBasket(basketId: Int): List<ProductConfiguration>
    suspend fun deleteConfiguration(configuration: ProductConfiguration)
    suspend fun insertConfiguration(configuration: ProductConfiguration)
    suspend fun insertConfigurations(configurations: List<ProductConfiguration>)
}
