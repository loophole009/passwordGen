package com.example.passwordgen.di

import android.content.Context
import androidx.room.Room
import com.example.passwordgen.db.LockerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLockerDatabase(@ApplicationContext context: Context): LockerDatabase {
        return Room.databaseBuilder(context, LockerDatabase::class.java, "locker_database").build()
    }
}