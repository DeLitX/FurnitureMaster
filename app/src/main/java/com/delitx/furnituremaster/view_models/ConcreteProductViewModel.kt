package com.delitx.furnituremaster.view_models

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data.BasketsRepository
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data.network.ServerRequestsImpl
import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.ProductPropertiesSet
import com.delitx.furnituremaster.data_models.ProductProperty
import com.delitx.furnituremaster.data_models.PropertyValue
import com.delitx.furnituremaster.data_models.dtos.BasketDTO
import com.delitx.furnituremaster.data_models.dtos.ProductConfiguration
import com.delitx.furnituremaster.data_models.price_recognition.PriceEvaluator
import com.delitx.furnituremaster.exceptions.WrongDataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConcreteProductViewModel @Inject constructor(
    private val mApp: Application,
    private val mBasketRepository: BasketsRepository,
    private val mProductsRepository: ProductsRepository
) : ViewModel() {
    val product: MutableState<Product?> = mutableStateOf(null)
    val configuration = mutableStateOf(ProductPropertiesSet(set = mutableMapOf()))
    val price = mutableStateOf(mApp.getString(R.string.choose_configuration))
    private var mCurrentBasket: BasketDTO? = null
    private var mProductsConfigurations: MutableList<ProductConfiguration>? = null

    // it will be used only to delete from db old configuration
    private var mStartConfiguration: ProductPropertiesSet? = null
    private var mBelongsToBasket: Int? = null

    init {
        viewModelScope.launch {
            withContext(IO) {
                mCurrentBasket = mBasketRepository.getCurrentBasket()
                mProductsConfigurations =
                    mBasketRepository.getConfigurationsByBasket(mCurrentBasket!!.id).toMutableList()
                calculatePrice()
            }
        }
    }

    fun loadFile(link: String, file: File, onSuccess: (link: Uri) -> Unit) {
        ServerRequestsImpl().downloadFileLink(link, file, onSuccess)
    }

    fun addToBasket(): Boolean {

        return if (mCurrentBasket == null) { // if basket is null or if basket already contains exactly this product
            false
        } else {
            val similarConfiguration = mProductsConfigurations?.find {
                it.productId == product.value?.id &&
                    it.chosenConfiguration == product.value?.chosenConfiguration ?: ProductPropertiesSet(
                    mutableMapOf()
                )
            }
            val configuration = ProductConfiguration(
                basketId = mCurrentBasket!!.id,
                productId = product.value!!.id,
                chosenConfiguration = configuration.value,
                amount = similarConfiguration?.amount ?: 1
            )
            mProductsConfigurations?.remove(similarConfiguration)
            mProductsConfigurations?.add(
                configuration
            )
            CoroutineScope(IO).launch {
                if (mCurrentBasket != null && mStartConfiguration != null) {
                    mBasketRepository.deleteConfiguration(
                        ProductConfiguration(
                            productId = product.value!!.id,
                            basketId = mCurrentBasket!!.id,
                            chosenConfiguration = mStartConfiguration!!
                        )
                    )
                    // assigning null to allow add to basket multiple configurations
                    mStartConfiguration = null
                }
                // deletion deletes another copy but with different values order
                mBasketRepository.deleteConfiguration(configuration)
                mBasketRepository.insertConfiguration(configuration)
            }
            true
        }
    }

    fun setConfiguration(configuration: ProductPropertiesSet) {
        this.configuration.value = configuration
        viewModelScope.launch {
            calculatePrice()
        }
    }

    fun setProduct(productId: Int, configuration: ProductPropertiesSet?, basketId: Int) {
        mProductsRepository.getLiveProductByName(productId).observeForever {
            it?.let {
                CoroutineScope(IO).launch {
                    product.value = mProductsRepository.productDtoToProduct(listOf(it))[0]
                }
            }
        }
        configuration?.let {
            setConfiguration(it)
        }
        mBelongsToBasket =
            if (basketId != -1) basketId else mBasketRepository.getSavedCurrentBasketId()
        mStartConfiguration = configuration
    }

    fun choosePropertyValueWithoutUpdate(property: ProductProperty, value: PropertyValue) {
        configuration.value.set[property.id] = value
        viewModelScope.launch {
            calculatePrice()
        }
    }

    fun choosePropertyValue(property: ProductProperty, value: PropertyValue) {
        val set = mutableMapOf<Int, PropertyValue>()
        for (i in configuration.value.set) {
            set[i.key] = i.value
        }
        set[property.id] = value
        setConfiguration(ProductPropertiesSet(set))
    }

    suspend fun calculatePrice() {
        val product = product.value
        product?.let {
            it.chosenConfiguration = configuration.value
            Log.d("PriceEvaluate", "setConfiguration: product $product")
            try {
                val temp = PriceEvaluator.evaluate(it)
                price.value = "%.2f".format(temp)
            } catch (e: WrongDataException) {
                price.value = mApp.applicationContext.getString(R.string.choose_configuration)
            }
        }
    }
}
