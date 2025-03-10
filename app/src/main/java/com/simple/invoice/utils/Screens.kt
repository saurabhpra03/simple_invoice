package com.simple.invoice.utils

sealed class Screens(val route: String) {

    data object Auth : Screens("nav_auth"){
        data object Login : Screens("login")
        data object Signup : Screens("sign_up")
    }

    data object Home : Screens("nav_home"){
        data object Invoices : Screens("invoices")
        data object InvoiceDetails: Screens("invoice_details/{id}")
        data object CreateInvoice : Screens("create_invoice")
        data object GenerateInvoice : Screens("generate_invoice/{sub_total}/{items}")
    }
}