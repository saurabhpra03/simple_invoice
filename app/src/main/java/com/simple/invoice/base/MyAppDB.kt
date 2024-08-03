package com.simple.invoice.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.simple.invoice.data.model.Auth
import com.simple.invoice.data.model.dao.AuthDao

@Database(entities = [Auth::class], version = 1)
abstract class MyAppDB : RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object {

        @Volatile
        private var INSTANCE: MyAppDB? = null

        fun getDatabase(context: Context): MyAppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDB::class.java,
                    "simple_invoice.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}