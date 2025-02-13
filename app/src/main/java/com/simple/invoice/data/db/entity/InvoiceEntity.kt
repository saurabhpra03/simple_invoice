package com.simple.invoice.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "invoice", foreignKeys = [
    ForeignKey(
        entity = AuthEntity::class,
        parentColumns = ["id"],
        childColumns = ["authId"],
    )
])
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val authId: Int,
    val invoiceNo: String,
    val date: Long,
    val items: String,
    val name: String,
    val subTotal: Double,
    val gst: String,
    val extraCharges: Double,
    val discount: Double,
    val discountType: String,
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis()
)
