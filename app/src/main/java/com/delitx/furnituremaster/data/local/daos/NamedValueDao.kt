package com.delitx.furnituremaster.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.NamedValue

@Dao
interface NamedValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NamedValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<NamedValue>)

    @Query("select * from named_value")
    suspend fun getAll(): List<NamedValue>

    @Query("select * from named_value where id in (:ids)")
    suspend fun getByIds(ids: List<Int>): List<NamedValue>

    @Query("select * from named_value where id=:id")
    fun getById(id: Int): LiveData<NamedValue>

    @Query("delete from named_value")
    fun deleteAll()

    @Delete
    fun delete(item: NamedValue)
    @Transaction
    suspend fun getByIdsBulk(ids: List<Int>): List<NamedValue> {
        return performBulkActionSuspend(ids, ::getByIds)
    }
}
