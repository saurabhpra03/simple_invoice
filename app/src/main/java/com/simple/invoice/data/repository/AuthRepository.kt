package com.simple.invoice.data.repository

import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.AuthEntity

interface AuthRepository {
    suspend fun addUser(auth: AuthEntity): Resource<AuthEntity>
    suspend fun login(emailId: String, password: String): Resource<AuthEntity>
}