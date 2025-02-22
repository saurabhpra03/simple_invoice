package com.simple.invoice.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.simple.invoice.data.db.entity.AuthEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val activity: Context) {

    private val PREF_NAME = "SimpleInvoicePref"
    private val KEY_AUTH = "auth"

    fun saveAuth(auth: AuthEntity) {
        val data = Gson().toJson(auth)
        val editor = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()
        editor.putString(KEY_AUTH, data)
        editor.apply()
    }

    fun getAuth(): AuthEntity? {
        val pref = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val data = pref.getString(KEY_AUTH, null)
        val auth = Gson().fromJson(data, AuthEntity::class.java)
        return auth
    }

}