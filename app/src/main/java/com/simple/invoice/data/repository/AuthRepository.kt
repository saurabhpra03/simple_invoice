package com.simple.invoice.data.repository

import com.simple.invoice.data.model.Auth
import com.simple.invoice.data.model.dao.AuthDao
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authDao: AuthDao) {

    suspend fun addUser(auth: Auth) = authDao.add(auth)

    suspend fun login(emailId: String, password: String) = authDao.login(emailId, password)

}