package com.delitx.furnituremaster.data.local.daos

import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.CommentedImage

@Dao
interface CommentedImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CommentedImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<CommentedImage>)

    @Query("select * from commentedimage where id in (:ids)")
    fun getByIds(ids: List<Int>): List<CommentedImage>

    @Query("delete from commentedimage")
    fun deleteAll()

    @Transaction
    suspend fun getByIdsBulk(ids: List<Int>): List<CommentedImage> {
        return performBulkActionSuspend(ids, ::getByIds)
    }
}
