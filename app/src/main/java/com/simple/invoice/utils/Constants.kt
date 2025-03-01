package com.simple.invoice.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController
import com.simple.invoice.R
import java.text.SimpleDateFormat
import java.util.Locale

object Constants {

    fun NavController.finishAndGotoNextScreen(route: String) {
        this.popBackStack()
        this.navigate(route) {
            launchSingleTop = true
        }
    }

    fun <A : Activity> Context.finishAndGotoNextActivity(activity: Class<A>) {
        Intent(this, activity).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    fun calculateItemTotalAmount(quantity: Int, price: Double): String{
        return Validator.getValidatedNumber("${(price * quantity).toBigDecimal()}",20)
    }

    fun calculateGST(gst: String, subTotal: String): String {
        return if (gst != "0%") {
            Validator.getValidatedNumber("${subTotal.toBigDecimal() * gst.replace("%", "").toBigDecimal() / 100.toBigDecimal()}")
        } else {
            "0.00"
        }
    }

    fun calculateDiscount(context: Context, discountOption: String, discount: String, subTotal: String): String {
        return when (discountOption) {
            context.getString(R.string.ruppe_symbol) -> Validator.getValidatedNumber(discount)
            context.getString(R.string.percentage_symbol) -> {
                if (discount.isNotEmpty()) {
                    Validator.getValidatedNumber("${subTotal.toBigDecimal() * (discount.toDouble() / 100).toBigDecimal()}")
                } else "0.00"
            }
            else -> "0.00"
        }
    }

    fun Long.convertTimeInMillisToDate(): String{
        val dateFormatter = SimpleDateFormat("dd-MMM-yyy",Locale.getDefault())
        return dateFormatter.format(this)

    }


}