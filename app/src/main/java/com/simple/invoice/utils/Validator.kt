package com.simple.invoice.utils

import android.content.Context
import android.util.Patterns
import com.simple.invoice.R
import java.math.BigDecimal
import java.math.RoundingMode

object Validator {

    fun isValidEmailId(context: Context, emailId: String) : String{
        return when{
            emailId.isEmpty() -> context.getString(R.string.empty_email_id)
            !Patterns.EMAIL_ADDRESS.matcher(emailId).matches() -> context.getString(R.string.invalid_email_id)
            else -> ""
        }
    }

    fun isValidPassword(context: Context, password: String) : String{
        return when{
            password.isEmpty() -> context.getString(R.string.empty_password)
            password.length < 5 -> context.getString(R.string.invalid_password)
            else -> ""
        }
    }

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