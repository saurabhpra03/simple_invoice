package com.simple.invoice.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.repository.InvoiceRepository
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GenerateInvoiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: InvoiceRepository
): ViewModel() {

    private val _createInvoice = MutableStateFlow<Resource<String>?>(null)
    val createInvoice: StateFlow<Resource<String>?> get() = _createInvoice

    val gstList = listOf("0%", "5%", "12%", "18%", "28%")
    var selectedGST by mutableStateOf(gstList[0])
    var gstAmount by mutableStateOf("0.00")

    var extraCharges by mutableStateOf("")
    val discountOptions = listOf(R.string.none, R.string.ruppe_symbol, R.string.percentage_symbol)
    var selectedDiscountOption by mutableIntStateOf(discountOptions[0])
    var discount by mutableStateOf("")
    var discountAmount by mutableStateOf("0.00")

    var subTotal by mutableStateOf("0.00")
    var totalAmount by mutableStateOf("0.00")

    fun calculateGST() {
        gstAmount = Constants.calculateGST(selectedGST, subTotal)
        calculateTotalAmount()
    }

    fun calculateDiscount() {
        discountAmount = Constants.calculateDiscount(context, context.getString(selectedDiscountOption), discount, subTotal)
        calculateTotalAmount()
    }

    fun calculateTotalAmount() {
        val gst = gstAmount.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        val extra = extraCharges.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        val discountAmt = discountAmount.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        totalAmount = Validator.getValidatedNumber("${subTotal.toBigDecimal() + gst + extra - discountAmt}")
    }


    fun addInvoice(invoice: InvoiceEntity) = viewModelScope.launch {
        _createInvoice.value = Resource.Loading
        _createInvoice.value = repository.addInvoice(invoice)
    }

    fun resetCreateInvoiceFlow(){
        _createInvoice.value = null
    }
}