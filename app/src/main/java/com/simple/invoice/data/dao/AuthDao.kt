package com.simple.invoice.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.invoice.data.model.Auth


@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(auth: Auth): Long

    @Query("SELECT * FROM auth WHERE emailId = :emailId AND password = :password")
    suspend fun login(emailId: String, password: String): Auth?

}