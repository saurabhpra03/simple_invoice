package com.simple.invoice.data.repository

import android.content.Context
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.dao.AuthDao
import com.simple.invoice.data.db.entity.AuthEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dao: AuthDao
) : AuthRepository {

    override suspend fun addUser(auth: AuthEntity): Resource<AuthEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = dao.add(auth)
                if (response > 0) {
                    auth.id = response.toInt()
                    Resource.Success(auth)
                } else {
                    Resource.Failed(context.getString(R.string.email_id_or_account_already_exists))
                }
            } catch (e: Exception) {
                Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }

    override suspend fun login(emailId: String, password: String): Resource<AuthEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = dao.login(emailId, password)
                response?.let {
                    Resource.Success(it)
                } ?: run {
                    Resource.Failed(context.getString(R.string.login_failed))
                }
            } catch (e: Exception) {
                Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }
}