package com.example.passwordgen.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.passwordgen.models.Locker

@Dao
interface LockerDao {

    @Query("SELECT * from lockers")
    suspend fun getLockers(): List<Locker>

    @Query("SELECT password from lockers where website == :website")
    suspend fun getLockerPassword(website: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocker(locker: Locker)

    @Delete
    suspend fun deleteLocker(locker: Locker)


}