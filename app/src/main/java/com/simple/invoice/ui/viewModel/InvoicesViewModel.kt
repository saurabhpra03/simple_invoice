package com.simple.invoice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.repository.InvoiceRepository
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoicesViewModel @Inject constructor(
    private val repository: InvoiceRepository,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val authId = sharedPref.getAuth()?.id ?: 0

    private val _invoices = MutableStateFlow<Resource<List<InvoiceEntity>>?>(null)
    val invoices: StateFlow<Resource<List<InvoiceEntity>>?> get() = _invoices

    private val _invoice = MutableStateFlow<Resource<InvoiceEntity>?>(null)
    val invoice: StateFlow<Resource<InvoiceEntity>?> get() = _invoice

    private val _deleteInvoice = MutableStateFlow<Resource<String>?>(null)
    val deleteInvoice: StateFlow<Resource<String>?> get() = _deleteInvoice


    fun fetchInvoices() = viewModelScope.launch {
        _invoices.value = Resource.Loading
        _invoices.value = repository.getInvoices(authId)
    }

    fun fetchInvoice(id: Int) = viewModelScope.launch {
        _invoice.value = Resource.Loading
        _invoice.value = repository.getInvoice(authId, id)
    }

    fun deleteInvoice(invoice: InvoiceEntity) = viewModelScope.launch {
        _deleteInvoice.value = Resource.Loading
        _deleteInvoice.value = repository.deleteInvoice(invoice)
    }

    fun resetInvoiceDeleteFlow() {
        _deleteInvoice.value = null
    }

}