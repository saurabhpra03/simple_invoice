package com.simple.invoice.data

sealed class Resource {
    data object Loading: Resource()
    data class Success(val data: Any): Resource()
    data class Failed(val msg: String): Resource()
}