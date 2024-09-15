package com.simple.invoice.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.simple.invoice.data.model.Invoice
import com.simple.invoice.data.model.InvoiceItems

@Dao
interface InvoiceDao {

    @Insert
    suspend fun addInvoice(invoice: Invoice) : Long

    @Insert
    suspend fun addInvoiceItems(invoiceItems: InvoiceItems): Long

    @Delete
    suspend fun deleteInvoice(invoice: Invoice)

}