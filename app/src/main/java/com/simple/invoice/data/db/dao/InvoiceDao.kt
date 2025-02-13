package com.simple.invoice.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.simple.invoice.data.db.entity.InvoiceEntity

@Dao
interface InvoiceDao {

    @Insert
    suspend fun addInvoice(invoice: InvoiceEntity) : Long

    @Query("SELECT * FROM invoice WHERE authId = :authId ORDER BY id DESC")
    suspend fun getInvoices(authId: Int): List<InvoiceEntity>

    @Delete
    suspend fun deleteInvoice(invoice: InvoiceEntity)

}