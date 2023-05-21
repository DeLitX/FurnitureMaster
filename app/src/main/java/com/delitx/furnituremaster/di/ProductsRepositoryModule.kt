package com.delitx.furnituremaster.di

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.data.ProductsRepositoryImpl
import com.delitx.furnituremaster.data.VersionCheck
import com.delitx.furnituremaster.data.local.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsRepositoryModule {
    @Singleton
    @Provides
    fun provideProductsRepository(
        productDao: ProductDao,
        propertyDao: PropertyDao,
        numeralValueDao: NumeralValueDao,
        namedValueDao: NamedValueDao,
        productPropertiesVariationDao: ProductPropertiesVariationDao,
        productToImageDao: ProductToImageDao,
        commentedImageDao: CommentedImageDao,
        application: Application
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            mNumeralValueDao = numeralValueDao,
            mProductDao = productDao,
            mPropertyDao = propertyDao,
            mNamedValueDao = namedValueDao,
            mProductPropertiesVariationDao = productPropertiesVariationDao,
            mProductToImageDao = productToImageDao,
            mCommentedImageDao = commentedImageDao,
            mSharedPreferences = application.getSharedPreferences("FurnitureMaster", MODE_PRIVATE),
            mVersionCheck = object : VersionCheck {
                override fun getAppVersion(): Int {
                    val info = application.packageManager.getPackageInfo(
                        application.packageName,
                        PackageManager.GET_ACTIVITIES
                    )
                    return info.versionCode
                }

                override fun openPlayMarket() {
                    val packageName = application.packageName
                    try {
                        application.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        application.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }
                }
            }
        )
    }
}
