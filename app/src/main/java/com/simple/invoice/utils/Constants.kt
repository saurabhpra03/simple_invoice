package com.simple.invoice.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController
import java.math.BigDecimal
import java.math.RoundingMode

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
        return getValidatedNumber("${(price * quantity).toBigDecimal()}",20)
    }

    /**
     * Filters a given numeric string to ensure:
     * - Only the first decimal point is allowed.
     * - At most three digits before the decimal and two digits after are retained.
     *
     * @param value The original numeric string input.
     * @return The validated and formatted numeric string.
     */
    fun getValidatedNumber(input: String, beforeDecimalDigits: Int = 7): String {
        // Remove unwanted characters, allowing only digits and a single decimal point
        val filteredChars = input.filterIndexed { index, char ->
            char.isDigit() || (char == '.' && input.indexOf('.') == index)  // Only take the first decimal point
        }

        // If the filtered input contains a decimal point, process the decimal part
        return if (filteredChars.contains('.')) {
            val beforeDecimal = filteredChars.substringBefore('.')
            val afterDecimal = filteredChars.substringAfter('.')

            // Combine before and after decimal parts with proper rounding
            val number = BigDecimal("$beforeDecimal.$afterDecimal")
            val roundedNumber = number.setScale(2, RoundingMode.HALF_UP)  // Round to 2 decimal places, rounding half up

            // Return the formatted result
            roundedNumber.toString()
        } else {
            // No decimal point: only take up to 7 digits
            filteredChars.take(beforeDecimalDigits)
        }
    }


}