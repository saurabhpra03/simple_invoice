package com.simple.invoice.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "auth", indices = [Index(value = ["emailId"], unique = true)])
data class AuthEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val emailId: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
