package com.delitx.furnituremaster.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data_models.NamedValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZoomablePagerViewModel @Inject constructor(private val mProductsRepository: ProductsRepository) : ViewModel() {
    fun getCollection(id: Int): LiveData<NamedValue> {
        return mProductsRepository.getCollection(id)
    }
}
