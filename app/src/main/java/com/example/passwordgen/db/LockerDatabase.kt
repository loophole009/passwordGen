package com.example.passwordgen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passwordgen.models.Locker

@Database(entities = [Locker::class], version = 1)
abstract class LockerDatabase : RoomDatabase() {

    abstract fun getLockerDao(): LockerDao

}