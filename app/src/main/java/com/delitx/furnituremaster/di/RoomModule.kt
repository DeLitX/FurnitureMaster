package com.delitx.furnituremaster.di

import android.content.Context
import androidx.room.Room
import com.delitx.furnituremaster.data.local.AppDB
import com.delitx.furnituremaster.data.local.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(
            context,
            AppDB::class.java, AppDB.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideBasketDao(db: AppDB): BasketDao {
        return db.getBasketDao()
    }
    @Singleton
    @Provides
    fun provideProductDao(db: AppDB): ProductDao {
        return db.getProductDao()
    }
    @Singleton
    @Provides
    fun providePropertyDao(db: AppDB): PropertyDao {
        return db.getPropertyDao()
    }
    @Singleton
    @Provides
    fun provideNumeralValueDao(db: AppDB): NumeralValueDao {
        return db.getNumeralValueDao()
    }
    @Singleton
    @Provides
    fun provideTypedValueDao(db: AppDB): NamedValueDao {
        return db.getTypedValueDao()
    }
    @Singleton
    @Provides
    fun provideProductToPropertyDao(db: AppDB): ProductPropertiesVariationDao {
        return db.getProductToPropertyDao()
    }
    @Singleton
    @Provides
    fun provideProductToImageDao(db: AppDB): ProductToImageDao {
        return db.getProductToImageDao()
    }
    @Singleton
    @Provides
    fun provideCommentedImageDao(db: AppDB): CommentedImageDao {
        return db.getCommentedImageDao()
    }
    @Singleton
    @Provides
    fun provideProductConfigurationDao(db: AppDB): ProductConfigurationDao {
        return db.getProductConfigurationDao()
    }
}
