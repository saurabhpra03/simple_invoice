package com.simple.invoice.ui.utils

sealed class Screens(val route: String) {

    data object Auth : Screens("nav_auth"){
        data object Login : Screens("login")
        data object Signup : Screens("sign_up")
    }
}