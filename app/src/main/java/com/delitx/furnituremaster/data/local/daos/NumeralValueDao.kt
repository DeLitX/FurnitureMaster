package com.delitx.furnituremaster.data.local.daos

import androidx.room.*
import com.delitx.furnituremaster.data.local.daos.utils.performBulkActionSuspend
import com.delitx.furnituremaster.data_models.NumeralValue

@Dao
interface NumeralValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NumeralValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<NumeralValue>)

    @Query("select * from numeral_value")
    suspend fun getAll(): List<NumeralValue>

    @Query("select * from numeral_value where id in (:ids)")
    suspend fun getByIds(ids: List<Int>): List<NumeralValue>

    @Query("delete from numeral_value")
    fun deleteAll()

    @Delete
    fun delete(item: NumeralValue)
    @Transaction
    suspend fun getByIdsBulk(ids: List<Int>): List<NumeralValue> {
        return performBulkActionSuspend(ids, ::getByIds)
    }
}
