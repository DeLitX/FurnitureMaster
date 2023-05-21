package com.delitx.furnituremaster.view_models

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.furnituremaster.data.BasketsRepository
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data_models.*
import com.delitx.furnituremaster.data_models.dtos.BasketDTO
import com.delitx.furnituremaster.data_models.dtos.ProductConfiguration
import com.delitx.furnituremaster.data_models.price_recognition.PriceEvaluator
import com.delitx.furnituremaster.exceptions.WrongDataException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrentBasketViewModel @Inject constructor(
    private val mBasketsRepository: BasketsRepository,
    private val mProductsRepository: ProductsRepository
) : ViewModel() {
    private var mCurrentBasket: LiveData<BasketDTO> = mBasketsRepository.getCurrentBasketLive()
    val currentBasket: MutableState<Basket> = mutableStateOf(Basket())
    val deletedProducts = mutableStateListOf<Product>()
    val isCurrent = mutableStateOf(true)
    val id = mutableStateOf(0)

    init {
        observeCurrentBasket()
    }

    fun setCurrent(basketId: Int) {
        CoroutineScope(IO).launch {
            mBasketsRepository.saveNewCurrentBasketId(basketId)
            isCurrent.value = true
            id.value = basketId
            mCurrentBasket = mBasketsRepository.getCurrentBasketLive()
            withContext(Main) {
                observeCurrentBasket()
            }
        }
    }

    fun changeProductAmount(product: Product, amount: Int) {
        product.amount = amount
        CoroutineScope(IO).launch {
            mBasketsRepository.insertConfiguration(
                ProductConfiguration(
                    productId = product.id,
                    basketId = currentBasket.value.id,
                    chosenConfiguration = product.chosenConfiguration ?: ProductPropertiesSet(
                        mutableMapOf()
                    ),
                    amount = amount
                )
            )
        }
    }

    fun deleteProduct(product: Product, position: Int) {
        deletedProducts.add(product)
        val configuration = ProductConfiguration(
            productId = product.id,
            basketId = currentBasket.value.id,
            chosenConfiguration = product.chosenConfiguration ?: ProductPropertiesSet(
                mutableMapOf()
            ),
            amount = product.amount
        )
        CoroutineScope(IO).launch {
            mBasketsRepository.deleteConfiguration(configuration)
        }
    }

    fun setNotCurrent(id: Int) {
        mCurrentBasket = mBasketsRepository.getById(id)
        isCurrent.value = false
        this.id.value = id
        mCurrentBasket.observeForever {
            if (it != null) {
                viewModelScope.launch {
                    withContext(IO) {
                        currentBasket.value =
                            mBasketsRepository.convertBasketDtoToBasket(it, mProductsRepository)
                    }
                }
            }
        }
    }

    fun saveBasket() {
        val currentBasket = this.currentBasket.value
        viewModelScope.launch {
            val basketDTO = BasketDTO(
                id = currentBasket.id,
                phoneNumber = currentBasket.phoneNumber,
                name = currentBasket.name,
                comment = currentBasket.comment
            )
            withContext(IO) {
                mBasketsRepository.saveBasket(basketDTO)
            }
            mCurrentBasket = mBasketsRepository.getCurrentBasketLive()
        }
    }

    fun checkoutBasket(): Boolean {
        return mBasketsRepository.checkoutBasket()
    }

    private fun observeCurrentBasket() {
        mCurrentBasket.observeForever {
            Log.d("CurrentBasketViewModel", ": $it")
            if (it != null) {
                viewModelScope.launch {
                    withContext(IO) {
                        val basket =
                            mBasketsRepository.convertBasketDtoToBasket(it, mProductsRepository)
                        currentBasket.value = basket
                    }
                }
            } else {
                viewModelScope.launch {
                    withContext(IO) {
                        mBasketsRepository.initiateCurrentBasket()
                    }
                }
            }
        }
    }

    fun calculatePrice(product: Product, defaultString: String): MutableState<String> {
        val price = mutableStateOf("")
        viewModelScope.launch {
            Log.d("PriceEvaluate", "setConfiguration: product $product")
            try {
                val temp = PriceEvaluator.evaluate(product)
                price.value = "%.2f".format(temp).replace(',', '.')
            } catch (e: WrongDataException) {
                price.value = defaultString
            }
        }
        return price
    }
}
