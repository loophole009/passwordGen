package com.example.passwordgen

import android.app.Application
import com.example.passwordgen.db.LockerDatabase
import com.example.passwordgen.repository.LockerRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LockerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}