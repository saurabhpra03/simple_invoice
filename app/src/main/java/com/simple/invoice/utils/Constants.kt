package com.simple.invoice.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController

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
        return getValidatedNumber((price * quantity).toString())
    }

    /**
     * Filters a given numeric string to ensure:
     * - Only the first decimal point is allowed.
     * - At most three digits before the decimal and two digits after are retained.
     *
     * @param value The original numeric string input.
     * @return The validated and formatted numeric string.
     */
    fun getValidatedNumber(input: String): String {
        // Remove unwanted characters, allowing only digits and a single decimal point
        val filteredChars = input.filterIndexed { index, char ->
            char.isDigit() || (char == '.' && input.indexOf('.') == index)  // Only take first decimal point
        }

        // Ensure the final string meets the digit limit requirements
        return if (filteredChars.contains('.')) {
            val beforeDecimal = filteredChars.substringBefore('.')
            val afterDecimal = filteredChars.substringAfter('.')
            "${beforeDecimal.take(7)}.${afterDecimal.take(2)}"   // Limit: 3 digits before, 2 digits after decimal
        } else {
            filteredChars.take(7)  // No decimal point: only take up to 3 digits
        }
    }



}