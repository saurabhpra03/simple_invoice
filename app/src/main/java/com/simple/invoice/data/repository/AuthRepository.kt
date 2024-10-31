package com.simple.invoice.data.repository

import com.simple.invoice.data.db.entity.AuthEntity
import com.simple.invoice.data.db.dao.AuthDao
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authDao: AuthDao) {

    suspend fun addUser(auth: AuthEntity) = authDao.add(auth)

    suspend fun login(emailId: String, password: String) = authDao.login(emailId, password)

}