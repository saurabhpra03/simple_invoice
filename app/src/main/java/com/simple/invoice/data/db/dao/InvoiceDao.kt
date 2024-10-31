package com.simple.invoice.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.db.entity.InvoiceItemsEntity

@Dao
interface InvoiceDao {

    @Insert
    suspend fun addInvoice(invoice: InvoiceEntity) : Long

    @Insert
    suspend fun addInvoiceItems(invoiceItems: InvoiceItemsEntity): Long

    @Delete
    suspend fun deleteInvoice(invoice: InvoiceEntity)

}