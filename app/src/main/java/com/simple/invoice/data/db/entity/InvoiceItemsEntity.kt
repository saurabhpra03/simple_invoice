package com.simple.invoice.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "invoice_items",
    foreignKeys = [
    ForeignKey(
        entity = InvoiceEntity::class,
        parentColumns = ["id"],
        childColumns = ["invoiceId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
])
data class InvoiceItemsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val invoiceId: Int,
    val item: String,
    val qty: Int,
    val gst: String,
    val price: Double,
    val amount: Double
)
