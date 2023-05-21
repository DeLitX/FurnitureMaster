package com.delitx.furnituremaster.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.furnituremaster.data_models.dtos.ProductConfiguration

@Dao
interface ProductConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configuration: ProductConfiguration)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configuration: List<ProductConfiguration>)

    @Delete
    fun delete(configuration: ProductConfiguration)

    @Query("select * from product_configuration where basketId=:basketId")
    suspend fun getByBasket(basketId: Int): List<ProductConfiguration>

    @Query("select * from product_configuration where productId=:productId and basketId=:basketId")
    suspend fun getByBasketAndProduct(productId: Int, basketId: Int): List<ProductConfiguration>

    @Query("select * from product_configuration where basketId=:basketId")
    fun getByBasketLive(basketId: Int): LiveData<List<ProductConfiguration>>

    @Query("delete from product_configuration where basketId=:basketId")
    suspend fun deleteByBasket(basketId: Int)
}
