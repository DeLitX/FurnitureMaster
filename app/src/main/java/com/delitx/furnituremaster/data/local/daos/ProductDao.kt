package com.delitx.furnituremaster.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.dtos.ProductDTO

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ProductDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ProductDTO>)

    @Query("select * from product")
    suspend fun getAll(): List<ProductDTO>

    @Query("select * from product where id in (:ids)")
    suspend fun getByIds(ids: List<Int>): List<ProductDTO>

    @Query("select * from product where id ==:id")
    fun getLiveById(id: Int): LiveData<ProductDTO?>

    @Query("delete from product")
    fun deleteAll()

    @Delete
    fun delete(item: ProductDTO)

    @Query("select * from product where id in (select productId from productpropertiesvariation where valueId in (:ids))")
    suspend fun getProductsFromPropertiesValueIds(ids: List<Int>): List<ProductDTO>

    @Transaction
    suspend fun getByIdsBulk(ids: List<Int>): List<ProductDTO> {
        return performBulkActionSuspend(ids, ::getByIds)
    }

    @Transaction
    suspend fun getProductsFromPropertiesValueIdsBulk(ids: List<Int>): List<ProductDTO> {
        return performBulkActionSuspend(ids, ::getProductsFromPropertiesValueIds)
    }
}
