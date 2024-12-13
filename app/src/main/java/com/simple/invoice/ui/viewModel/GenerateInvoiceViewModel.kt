package com.simple.invoice.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.simple.invoice.R
import com.simple.invoice.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GenerateInvoiceViewModel @Inject constructor(): ViewModel() {
    var name by mutableStateOf("")
    var nameError by mutableStateOf("")

    val gstList = listOf("0%", "5%", "12%", "18%", "28%")
    var selectedGST by mutableStateOf(gstList[0])
    var gstAmount by mutableStateOf("0.00")

    var extraCharges by mutableStateOf("")
    val discountOptions = listOf(R.string.none, R.string.ruppe_symbol, R.string.percentage_symbol)
    var selectedDiscountOption by mutableStateOf(discountOptions[0])
    var discount by mutableStateOf("")
    var discountAmount by mutableStateOf("0.00")

    var subTotal by mutableStateOf("0.00")
    var totalAmount by mutableStateOf("0.00")

    fun calculateGST() {
        gstAmount = if (selectedGST != gstList[0]) {
            Constants.getValidatedNumber((subTotal.toDouble() * selectedGST.replace("%", "").toDouble() / 100).toString())
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
                    Constants.getValidatedNumber((subTotal.toDouble() * (discount.toDouble() / 100)).toString())
                } else "0.00"
            }
            else -> "0.00"
        }
        calculateTotalAmount()
    }

    fun calculateTotalAmount() {
        val gst = gstAmount.toDoubleOrNull() ?: 0.0
        val extra = extraCharges.toDoubleOrNull() ?: 0.0
        val discountAmt = discountAmount.toDoubleOrNull() ?: 0.0
        totalAmount = Constants.getValidatedNumber((subTotal.toDouble() + gst + extra - discountAmt).toString())
    }

    fun validateName(): Boolean {
        return if (name.isBlank()) {
            nameError = "Name cannot be empty."
            false
        } else {
            nameError = ""
            true
        }
    }


}