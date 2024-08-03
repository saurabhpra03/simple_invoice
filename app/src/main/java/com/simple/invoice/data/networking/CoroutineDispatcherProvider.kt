package com.simple.invoice.data.networking

import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherProvider {

    fun IO() = Dispatchers.IO

    fun Main() = Dispatchers.Main

    fun Default() = Dispatchers.Default

}