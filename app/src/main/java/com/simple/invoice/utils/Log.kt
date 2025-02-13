package com.simple.invoice.utils

import android.util.Log

object Log {

    fun String.logD(message: String) = Log.d(this, message)

    fun String.logE(message: String) = Log.e(this, message)

}