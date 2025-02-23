package com.simple.invoice.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import com.simple.invoice.data.repository.InvoiceRepository
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoicesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: InvoiceRepository,
    private val sharedPref: SharedPref,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val authId = sharedPref.getAuth()?.id ?: 0

    private val _invoices = MutableStateFlow<Resource<List<InvoiceEntity>>?>(null)
    val invoices: StateFlow<Resource<List<InvoiceEntity>>?> get() = _invoices

    private val _deleteInvoice = MutableStateFlow<Resource<String>?>(null)
    val deleteInvoice: StateFlow<Resource<String>?> get() = _deleteInvoice


    fun fetchInvoices() {
        _invoices.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response = repository.getInvoices(authId)
                _invoices.value = response?.let {
                    if (response.isNotEmpty()) {
                        Resource.Success(response)
                    } else {
                        Resource.Failed(context.getString(R.string.no_invoice_found))
                    }
                } ?: run {
                    Resource.Failed(context.getString(R.string.no_invoice_found))
                }
            } catch (e: Exception) {
                _invoices.value = Resource.Failed(
                    e.message ?: context.getString(R.string.something_went_wrong_please_try_again)
                )
            }
        }
    }

    fun deleteInvoice(invoice: InvoiceEntity) {
        _deleteInvoice.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                repository.deleteInvoice(invoice)
                _deleteInvoice.value =
                    Resource.Success(context.getString(R.string.invoice_deleted_successfully))
            } catch (e: Exception) {
                _deleteInvoice.value = Resource.Failed(
                    e.message ?: context.getString(R.string.something_went_wrong_please_try_again)
                )
            }
        }
    }

    fun resetInvoiceDeleteFlow() {
        _deleteInvoice.value = null
    }

}