package com.simple.invoice.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Auth(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val emailId: String,
    val password: String
)
