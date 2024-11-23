package com.simple.invoice.data.module

data class InvoiceDto(
    val id: Int,
    val item: String,
    val qty: Int,
    val unitPrice: String,
    val totalAmount: String
)
