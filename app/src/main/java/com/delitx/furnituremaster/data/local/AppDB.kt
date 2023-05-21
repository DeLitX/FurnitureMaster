package com.delitx.furnituremaster.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.delitx.furnituremaster.data.local.converters.Converters
import com.delitx.furnituremaster.data.local.daos.*
import com.delitx.furnituremaster.data_models.CommentedImage
import com.delitx.furnituremaster.data_models.NamedValue
import com.delitx.furnituremaster.data_models.NumeralValue
import com.delitx.furnituremaster.data_models.dtos.*

@Database(
    entities = [
        BasketDTO::class,
        ProductDTO::class,
        ProductPropertyDTO::class,
        NamedValue::class,
        NumeralValue::class,
        ProductPropertiesVariation::class,
        ProductToImage::class,
        CommentedImage::class,
        ProductConfiguration::class,
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "app_db"
        const val MAX_LIST_SIZE = 900
    }

    abstract fun getBasketDao(): BasketDao
    abstract fun getProductToImageDao(): ProductToImageDao
    abstract fun getCommentedImageDao(): CommentedImageDao
    abstract fun getProductDao(): ProductDao
    abstract fun getPropertyDao(): PropertyDao
    abstract fun getTypedValueDao(): NamedValueDao
    abstract fun getNumeralValueDao(): NumeralValueDao
    abstract fun getProductToPropertyDao(): ProductPropertiesVariationDao
    abstract fun getProductConfigurationDao(): ProductConfigurationDao
}
