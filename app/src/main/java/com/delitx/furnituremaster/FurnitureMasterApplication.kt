package com.delitx.furnituremaster

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FurnitureMasterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Disables dark mode in whole app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
