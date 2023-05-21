package com.delitx.furnituremaster.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.delitx.furnituremaster.data.local.daos.BasketDao
import com.delitx.furnituremaster.data.local.daos.ProductConfigurationDao
import com.delitx.furnituremaster.data_models.*
import com.delitx.furnituremaster.data_models.dtos.BasketDTO
import com.delitx.furnituremaster.data_models.dtos.ProductConfiguration

class BasketsRepositoryImpl(
    private val mSharedPreferences: SharedPreferences,
    private val mBasketDao: BasketDao,
    private val mProductConfigurationDao: ProductConfigurationDao
) : BasketsRepository {
    private val CURRENT_BASKET_ID = "current basket id"

    override suspend fun getCurrentBasket(): BasketDTO {
        val currentBasketId = getSavedCurrentBasketId()
        var basket = mBasketDao.getById(currentBasketId)
        if (basket == null) {
            basket = BasketDTO(id = currentBasketId)
            mBasketDao.insert(basket)
        }
        return basket
    }

    override fun getCurrentBasketLive(): LiveData<BasketDTO> {
        val currentBasketId = getSavedCurrentBasketId()
        return mBasketDao.getLiveById(currentBasketId)
    }

    override suspend fun initiateCurrentBasket() {
        val currentBasketId = getSavedCurrentBasketId()
        val basket = mBasketDao.getById(currentBasketId)
        if (basket == null) {
            mBasketDao.insert(BasketDTO(id = currentBasketId))
        }
    }

    override fun getBasketList(): LiveData<List<BasketDTO>> {
        return mBasketDao.getAll()
    }

    override fun saveCurrentBasketId(id: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(CURRENT_BASKET_ID, id)
        editor.apply()
    }

    override suspend fun saveNewCurrentBasketId(id: Int) {
        val oldId = getSavedCurrentBasketId()
        mProductConfigurationDao.deleteByBasket(oldId)
        mBasketDao.deleteById(oldId)
        saveCurrentBasketId(id)
    }

    override fun getSavedCurrentBasketId(): Int {
        return mSharedPreferences.getInt(CURRENT_BASKET_ID, 1)
    }

    override fun checkoutBasket(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): LiveData<BasketDTO> {
        return mBasketDao.getLiveById(id)
    }

    override fun getConfigurationsByBasketLive(basketId: Int): LiveData<List<ProductConfiguration>> {
        return mProductConfigurationDao.getByBasketLive(basketId)
    }

    override suspend fun saveBasket(basket: BasketDTO) {
        val currentId = getSavedCurrentBasketId()
        if (basket.id == currentId) {
            val lastId = mBasketDao.getLastId()
            saveCurrentBasketId(lastId + 1)
            val newCurrentBasket = BasketDTO(id = lastId + 1)
            mBasketDao.insert(listOf(basket, newCurrentBasket))
        } else {
            mBasketDao.insert(basket)
        }
    }

    override suspend fun updateBasket(basket: BasketDTO) {
        mBasketDao.insert(basket)
    }

    override suspend fun deleteBasket(basket: BasketDTO) {
        val currentBasketId = getSavedCurrentBasketId()
        if (basket.id == currentBasketId) {
            // we clear all previous data and set new current basket with the same id
            mBasketDao.insert(BasketDTO(currentBasketId))
        } else {
            mBasketDao.delete(basket)
        }
        mProductConfigurationDao.deleteByBasket(basket.id)
    }

    override suspend fun convertBasketDtoToBasket(
        dto: BasketDTO,
        repository: ProductsRepository
    ): Basket {
        val productsConfigurations = mProductConfigurationDao.getByBasket(dto.id)
        val productsNamesList = mutableListOf<Int>()
        for (i in productsConfigurations) {
            productsNamesList.add(i.productId)
        }
        val tempProducts = repository.getProductsByIds(productsNamesList)
        val products = mutableListOf<Product>()
        for (i in productsNamesList.indices) {
            val product = tempProducts.find { productsNamesList[i] == it.id }
            product?.let {
                products.add(
                    Product(
                        id = it.id,
                        name = it.name,
                        priceFormula = it.priceFormula,
                        imageUrl = it.imageUrl,
                        propertiesList = it.propertiesList,
                        chosenConfiguration = productsConfigurations[i].chosenConfiguration,
                        order = it.order,
                        amount = productsConfigurations[i].amount,
                        modelPath = it.modelPath
                    )
                )
            }
        }
        return Basket(
            id = dto.id,
            phoneNumber = dto.phoneNumber,
            name = dto.name,
            comment = dto.comment,
            products = products
        )
    }

    override suspend fun getConfigurationsByBasket(basketId: Int): List<ProductConfiguration> {
        return mProductConfigurationDao.getByBasket(basketId)
    }

    override suspend fun deleteConfiguration(configuration: ProductConfiguration) {
        val configurations = mProductConfigurationDao.getByBasketAndProduct(
            configuration.productId,
            configuration.basketId
        )
        val item = configurations.find {
            it.chosenConfiguration.set == configuration.chosenConfiguration.set
        }
        if (item != null) {
            mProductConfigurationDao.delete(item)
        }
    }

    override suspend fun insertConfiguration(configuration: ProductConfiguration) {
        mProductConfigurationDao.insert(configuration)
    }

    override suspend fun insertConfigurations(configurations: List<ProductConfiguration>) {
        mProductConfigurationDao.insert(configurations)
    }
}
