package com.simple.invoice.data.repository

import com.simple.invoice.data.db.dao.InvoiceDao
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.db.entity.InvoiceItemsEntity
import javax.inject.Inject

class InvoiceRepository @Inject constructor(private val invoiceDao: InvoiceDao) {

    suspend fun addInvoiceItems(invoiceItems: InvoiceItemsEntity) = invoiceDao.addInvoiceItems(invoiceItems)

    suspend fun addInvoice(invoice: InvoiceEntity) = invoiceDao.addInvoice(invoice)

    suspend fun deleteInvoice(invoice: InvoiceEntity) = invoiceDao.deleteInvoice(invoice)
}