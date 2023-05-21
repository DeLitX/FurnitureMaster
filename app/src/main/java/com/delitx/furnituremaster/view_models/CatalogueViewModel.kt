package com.delitx.furnituremaster.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data.network.BinotelRequests
import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.ProductProperty
import com.delitx.furnituremaster.data_models.PropertyValue
import com.delitx.furnituremaster.data_models.utils.getValuesByPropertyId
import com.delitx.furnituremaster.exceptions.WrongDataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(private val mProductsRepository: ProductsRepository) :
    ViewModel() {
    private val mProductsLiveData: LiveData<List<Product>> = mProductsRepository.getAllProducts()
    private var mAllProducts = listOf<Product>()
    val allProperties = mutableStateOf(mapOf<ProductProperty, List<PropertyValue>>())
    val chosenPropertiesValuesIds = mutableStateOf(mutableListOf<Int>())
    val productsToPresent = mutableStateOf(listOf<Product>())
    val collections = mutableStateOf(listOf<PropertyValue>())

    init {
        mProductsLiveData.observeForever {
            mAllProducts = it
            allProperties.value = getProperties(it)
            collections.value = getCollections(it)
            updateProducts()
        }
    }

    fun clearFilters() {
        chosenPropertiesValuesIds.value = mutableListOf()
    }

    fun updateProducts() {
        viewModelScope.launch {
            if (chosenPropertiesValuesIds.value.isEmpty()) {
                productsToPresent.value = mAllProducts
            } else {
                withContext(IO) {
                    val products =
                        mProductsRepository.getProductsByValueIds(chosenPropertiesValuesIds.value)
                    productsToPresent.value = products
                }
            }
        }
    }

    fun call(phoneNumber: String) {
        CoroutineScope(IO).launch {
            BinotelRequests().makeCall(phoneNumber)
        }
    }

    fun addChosenValue(id: Int) {
        val result = mutableListOf<Int>()
        result.addAll(chosenPropertiesValuesIds.value)
        result.add(id)
        chosenPropertiesValuesIds.value = result
        updateProducts()
    }

    fun removeChosenValue(id: Int) {
        val result = mutableListOf<Int>()
        result.addAll(chosenPropertiesValuesIds.value)
        result.remove(id)
        chosenPropertiesValuesIds.value = result
        updateProducts()
    }

    private fun getCollections(list: List<Product>): List<PropertyValue> {
        val result = mutableSetOf<PropertyValue>()
        for (i in list) {
            try {
                result.addAll(getValuesByPropertyId(i.propertiesList, 1))
            } catch (e: WrongDataException) {
                continue
            }
        }
        return result.toList()
    }

    private fun getProperties(products: List<Product>): Map<ProductProperty, List<PropertyValue>> {
        val result = mutableMapOf<ProductProperty, MutableList<PropertyValue>>()
        for (i in products) {
            for (t in i.propertiesList)
                if (result[t.key] == null) {
                    result[t.key] = t.value.toMutableList()
                } else {
                    result[t.key]!!.addAll(t.value.minus(result[t.key]!!))
                }
        }
        return result
    }
}
