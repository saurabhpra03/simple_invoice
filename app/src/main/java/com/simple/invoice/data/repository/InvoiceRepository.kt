package com.simple.invoice.data.repository

import com.simple.invoice.data.db.dao.InvoiceDao
import com.simple.invoice.data.db.entity.InvoiceEntity
import javax.inject.Inject

class InvoiceRepository @Inject constructor(private val invoiceDao: InvoiceDao) {

    suspend fun addInvoice(invoice: InvoiceEntity) = invoiceDao.addInvoice(invoice)

    suspend fun getInvoices(authId: Int) = invoiceDao.getInvoices(authId)

    suspend fun getInvoice(authId: Int, id: Int) = invoiceDao.getInvoice(authId, id)

    suspend fun deleteInvoice(invoice: InvoiceEntity) = invoiceDao.deleteInvoice(invoice)
}