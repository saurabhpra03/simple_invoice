package com.simple.invoice.data.repository

import android.content.Context
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.dao.InvoiceDao
import com.simple.invoice.data.db.entity.InvoiceEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: InvoiceDao
) : InvoiceRepository {

    override suspend fun addInvoice(invoice: InvoiceEntity): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val response = dao.addInvoice(invoice)
                if (response > 0) {
                    Resource.Success(context.getString(R.string.invoice_generated_successfully))
                } else {
                    Resource.Failed(context.getString(R.string.something_went_wrong_please_try_again))
                }
            } catch (e: Exception) {
                Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }

    override suspend fun getInvoices(authId: Int): Resource<List<InvoiceEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val response = dao.getInvoices(authId)
                response?.let {
                    if (response.isNotEmpty()) {
                        Resource.Success(response)
                    } else {
                        Resource.Failed(context.getString(R.string.no_invoice_found))
                    }
                } ?: run {
                    Resource.Failed(context.getString(R.string.no_invoice_found))
                }
            } catch (e: Exception) {
                Resource.Failed(
                    e.message ?: context.getString(R.string.something_went_wrong_please_try_again)
                )
            }
        }

    override suspend fun getInvoice(authId: Int, id: Int): Resource<InvoiceEntity> =
        withContext(Dispatchers.IO) {
            try {
                val response = dao.getInvoice(authId, id)
                response?.let {
                    Resource.Success(it)
                } ?: run {
                    Resource.Failed(context.getString(R.string.no_invoice_found))
                }
            } catch (e: Exception) {
                Resource.Failed(context.getString(R.string.something_went_wrong_please_try_again))
            }
        }

    override suspend fun deleteInvoice(invoice: InvoiceEntity): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                dao.deleteInvoice(invoice)
                Resource.Success(context.getString(R.string.invoice_deleted_successfully))
            } catch (e: Exception) {
                Resource.Failed(
                    e.message ?: context.getString(R.string.something_went_wrong_please_try_again)
                )
            }
        }
}