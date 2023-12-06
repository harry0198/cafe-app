package me.harrydrummond.cafeapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.harrydrummond.cafeapplication.logic.NotificationHelper

@HiltAndroidApp
class CafeApp: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper(this).setUpNotificationChannels()
    }

}