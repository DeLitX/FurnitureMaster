package com.delitx.furnituremaster.view_models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.furnituremaster.data.BasketsRepository
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data_models.Basket
import com.delitx.furnituremaster.data_models.dtos.BasketDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedBasketsViewModel @Inject constructor(
    private val mBasketsRepository: BasketsRepository,
    private val mProductsRepository: ProductsRepository
) : ViewModel() {
    private val mBasketList: LiveData<List<BasketDTO>> = mBasketsRepository.getBasketList()
    private lateinit var mBaskets: List<BasketDTO>
    val basketList = mutableStateOf(mutableListOf<Basket>())
    val deletedBaskets = mutableStateListOf<Basket>()
    val currentBasketId = mutableStateOf(mBasketsRepository.getSavedCurrentBasketId())

    init {
        mBasketList.observeForever {
            updateBasketList(it)
            mBaskets = it
        }
    }

    private fun updateBasketList(list: List<BasketDTO>) {
        viewModelScope.launch {
            withContext(IO) {
                val resultList: MutableList<Basket> =
                    list.map {
                        mBasketsRepository.convertBasketDtoToBasket(
                            it,
                            mProductsRepository
                        )
                    }.toMutableList()
                val tempList = mutableListOf<Basket>()
                val newItems = resultList.minus(basketList.value)
                val deletedItems = basketList.value.minus(resultList)
                tempList.addAll(basketList.value)
                tempList.addAll(newItems)
                basketList.value = resultList.toMutableList()
            }
        }
    }

    fun addBasket(basket: BasketDTO) {
        /*CoroutineScope(IO).launch {
            val item = mBasketsRepository.convertBasketDtoToBasket(basket, mProductsRepository)
            basketList.add(item)
        }*/
    }

    fun deleteBasket(basket: Basket) {
        deletedBaskets.add(basket)
        mBaskets.find { it.id == basket.id }?.let {
            CoroutineScope(IO).launch {
                mBasketsRepository.deleteBasket(it)
            }
        }
    }
}
