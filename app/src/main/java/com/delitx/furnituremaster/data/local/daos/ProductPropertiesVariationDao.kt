package com.delitx.furnituremaster.data.local.daos

import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.dtos.ProductPropertiesVariation

@Dao
interface ProductPropertiesVariationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ProductPropertiesVariation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<ProductPropertiesVariation>)

    @Query("select * from productpropertiesvariation")
    suspend fun getAll(): List<ProductPropertiesVariation>

    @Query("select  * from productpropertiesvariation where productId in (:ids)")
    suspend fun getByProductId(ids: List<Int>): List<ProductPropertiesVariation>

    @Query("select * from productpropertiesvariation where valueId in (:ids)")
    suspend fun getByValueId(ids: List<Int>): List<ProductPropertiesVariation>

    @Query("delete from productpropertiesvariation")
    fun deleteAll()

    @Transaction
    suspend fun getByProductIdBulk(ids: List<Int>): List<ProductPropertiesVariation> {
        return performBulkActionSuspend(ids, ::getByProductId)
    }
    @Transaction
    suspend fun getByValueIdBulk(ids: List<Int>): List<ProductPropertiesVariation> {
        return performBulkActionSuspend(ids, ::getByValueId)
    }
}
