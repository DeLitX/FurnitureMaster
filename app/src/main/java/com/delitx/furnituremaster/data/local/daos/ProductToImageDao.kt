package com.delitx.furnituremaster.data.local.daos

import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.dtos.ProductToImage

@Dao
interface ProductToImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ProductToImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ProductToImage>)

    @Query("select * from producttoimage where productId in (:ids)")
    fun getByProductIds(ids: List<Int>): List<ProductToImage>

    @Query("delete from producttoimage")
    fun deleteAll()

    @Transaction
    suspend fun getByProductIdsBulk(ids: List<Int>): List<ProductToImage> {
        return performBulkActionSuspend(ids, ::getByProductIds)
    }
}
