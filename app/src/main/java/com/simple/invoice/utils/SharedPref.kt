package com.simple.invoice.utils

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.simple.invoice.data.model.Auth
import javax.inject.Inject

class SharedPref @Inject constructor(private val activity: Activity) {

    private val PREF_NAME = "SimpleInvoicePref"
    private val KEY_AUTH = "auth"

    fun saveAuth(auth: Auth) {
        val data = Gson().toJson(auth)
        val editor = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()
        editor.putString(KEY_AUTH, data)
        editor.apply()
    }

    fun getAuth(): Auth? {
        val pref = activity.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val data = pref.getString(KEY_AUTH, null)
        val auth = Gson().fromJson(data, Auth::class.java)
        return auth
    }

}