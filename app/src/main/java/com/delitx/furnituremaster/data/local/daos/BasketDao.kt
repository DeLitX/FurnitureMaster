package com.delitx.furnituremaster.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.delitx.furnituremaster.data_models.dtos.BasketDTO

@Dao
interface BasketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(basket: BasketDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(baskets: List<BasketDTO>)

    @Query("select * from basket")
    fun getAll(): LiveData<List<BasketDTO>>

    @Query("select * from basket where id=:id")
    suspend fun getById(id: Int): BasketDTO

    @Query("select * from basket where id=:id")
    fun getLiveById(id: Int): LiveData<BasketDTO>
    @Query("select id from basket order by id desc")
    suspend fun getLastId(): Int
    @Query("delete from basket where id=:basketId")
    suspend fun deleteById(basketId: Int): Int

    @Delete
    suspend fun delete(basket: BasketDTO)

    @Query("delete from basket")
    fun deleteAll()
}
