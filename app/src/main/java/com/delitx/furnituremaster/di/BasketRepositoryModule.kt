package com.delitx.furnituremaster.di

import android.content.Context
import android.content.SharedPreferences
import com.delitx.furnituremaster.FurnitureMasterApplication
import com.delitx.furnituremaster.data.BasketsRepository
import com.delitx.furnituremaster.data.BasketsRepositoryImpl
import com.delitx.furnituremaster.data.local.daos.BasketDao
import com.delitx.furnituremaster.data.local.daos.ProductConfigurationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BasketRepositoryModule {
    @Singleton
    @Provides
    fun provideBasketsRepository(sharedPreferences: SharedPreferences, basketDao: BasketDao, productConfigurationDao: ProductConfigurationDao): BasketsRepository {
        return BasketsRepositoryImpl(sharedPreferences, basketDao, productConfigurationDao)
    }
    @Singleton
    @Provides
    fun provideSharedPreferences(app: FurnitureMasterApplication): SharedPreferences {
        return app.getSharedPreferences("shared prefs", Context.MODE_PRIVATE)
    }
}
