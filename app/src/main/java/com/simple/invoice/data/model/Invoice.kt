package com.simple.invoice.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Auth::class,
        parentColumns = ["id"],
        childColumns = ["authID"],
    )
])
data class Invoice(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val authId: Int,
    val invoiceNo: String,
    val date: Long,
    val subTotal: Double,
    val extraCharges: Double,
    val discount: Double,
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis()
)
