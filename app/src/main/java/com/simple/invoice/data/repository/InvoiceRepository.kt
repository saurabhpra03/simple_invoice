package com.simple.invoice.data.repository

import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity

interface InvoiceRepository {

    suspend fun addInvoice(invoice: InvoiceEntity): Resource<String>
    suspend fun getInvoices(authId: Int): Resource<List<InvoiceEntity>>
    suspend fun getInvoice(authId: Int, id: Int): Resource<InvoiceEntity>
    suspend fun deleteInvoice(invoice: InvoiceEntity): Resource<String>
}