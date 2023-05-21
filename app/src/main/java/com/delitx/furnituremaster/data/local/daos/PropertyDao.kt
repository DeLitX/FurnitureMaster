package com.delitx.furnituremaster.data.local.daos

import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.dtos.ProductPropertyDTO

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ProductPropertyDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<ProductPropertyDTO>)

    @Query("select * from product_property")
    suspend fun getAll(): List<ProductPropertyDTO>

    @Query("select * from product_property where id in (:ids)")
    suspend fun getByIds(ids: List<Int>): List<ProductPropertyDTO>

    @Query("delete from product_property")
    fun deleteAll()

    @Delete
    fun delete(item: ProductPropertyDTO)

    @Transaction
    suspend fun getByIdsBulk(ids: List<Int>): List<ProductPropertyDTO> {
        return performBulkActionSuspend(ids, ::getByIds)
    }
}
