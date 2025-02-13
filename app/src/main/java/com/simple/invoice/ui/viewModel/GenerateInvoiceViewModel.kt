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
import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import com.simple.invoice.data.repository.InvoiceRepository
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Log.logE
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GenerateInvoiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPref: SharedPref,
    private val repository: InvoiceRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
): ViewModel() {

    private val TAG = GenerateInvoiceViewModel::class.java.simpleName

    private val _addInvoiceFlow = MutableStateFlow<Resource<String>?>(null)
    val addInvoiceFlow: StateFlow<Resource<String>?> get() = _addInvoiceFlow

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
        gstAmount = if (selectedGST != gstList[0]) {
            Constants.getValidatedNumber("${subTotal.toBigDecimal() * selectedGST.replace("%", "").toBigDecimal() / 100.toBigDecimal()}")
        } else {
            "0.00"
        }
        calculateTotalAmount()
    }

    fun calculateDiscount() {
        discountAmount = when (selectedDiscountOption) {
            discountOptions[1] -> Constants.getValidatedNumber(discount)
            discountOptions[2] -> {
                if (discount.isNotEmpty()) {
                    Constants.getValidatedNumber("${subTotal.toBigDecimal() * (discount.toDouble() / 100).toBigDecimal()}")
                } else "0.00"
            }
            else -> "0.00"
        }
        calculateTotalAmount()
    }

    fun calculateTotalAmount() {
        val gst = gstAmount.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        val extra = extraCharges.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        val discountAmt = discountAmount.toBigDecimalOrNull() ?: 0.0.toBigDecimal()
        totalAmount = Constants.getValidatedNumber("${subTotal.toBigDecimal() + gst + extra - discountAmt}")
    }


    fun addInvoice(invoice: InvoiceEntity){

        _addInvoiceFlow.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response = repository.addInvoice(invoice)
               _addInvoiceFlow.value =  if (response > 0){
                    Resource.Success(context.getString(R.string.invoice_generated_successfully))
                }else{
                    TAG.logE("addInvoice, error, response: $response")
                    Resource.Failed(context.getString(R.string.something_went_wrong_please_try_again))
                }
            }catch (e: Exception){
                TAG.logE("addInvoice, exception: ${e.message}")
                _addInvoiceFlow.value = Resource.Failed(
                    e.message ?: context.getString(R.string.error_occurred_try_again)
                )
            }
        }
    }
}