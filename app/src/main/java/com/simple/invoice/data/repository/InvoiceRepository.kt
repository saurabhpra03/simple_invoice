package com.simple.invoice.data.repository

import com.simple.invoice.data.dao.InvoiceDao
import com.simple.invoice.data.model.Invoice
import com.simple.invoice.data.model.InvoiceItems
import javax.inject.Inject

class InvoiceRepository @Inject constructor(private val invoiceDao: InvoiceDao) {

    suspend fun addInvoiceItems(invoiceItems: InvoiceItems) = invoiceDao.addInvoiceItems(invoiceItems)

    suspend fun addInvoice(invoice: Invoice) = invoiceDao.addInvoice(invoice)

    suspend fun deleteInvoice(invoice: Invoice) = invoiceDao.deleteInvoice(invoice)
}