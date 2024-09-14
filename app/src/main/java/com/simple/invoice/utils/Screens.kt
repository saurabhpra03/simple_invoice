package com.simple.invoice.utils

sealed class Screens(val route: String) {

    data object Auth : Screens("nav_auth"){
        data object Login : Screens("login")
        data object Signup : Screens("sign_up")
    }

    data object Home : Screens("nav_home"){
        data object Invoices : Screens("invoices")
        data object CreateInvoice : Screens("create_invoice")
    }
}