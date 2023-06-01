package com.example.passwordgen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lockers")
data class Locker(
    @PrimaryKey()
    val website: String,
    val username: String,
    val password: String,
    val timestamp: String
)
