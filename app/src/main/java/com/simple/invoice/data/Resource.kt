package com.simple.invoice.data

sealed class Resource<out R> {
    data object Loading: Resource<Nothing>()
    data class Success<out R>(val data: R): Resource<R>()
    data class Failed(val msg: String): Resource<Nothing>()
}