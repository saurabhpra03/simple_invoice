package com.simple.invoice.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simple.invoice.data.db.entity.AuthEntity


@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(auth: AuthEntity): Long

    @Query("SELECT * FROM auth WHERE emailId = :emailId AND password = :password")
    suspend fun login(emailId: String, password: String): AuthEntity?

}