package com.simple.invoice.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["emailId"], unique = true)])
data class Auth(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val emailId: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
