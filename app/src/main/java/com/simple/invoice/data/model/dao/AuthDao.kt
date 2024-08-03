package com.simple.invoice.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.simple.invoice.data.model.Auth


@Dao
interface AuthDao {

    @Insert
    suspend fun add(auth: Auth)

    @Query("SELECT COUNT() FROM auth WHERE emailId = :emailId")
    suspend fun isUserExists(emailId: String) : Int

    @Query("SELECT COUNT() FROM auth WHERE emailId = :emailId AND password = :password")
    suspend fun login(emailId: String, password: String): Int

}