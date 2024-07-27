package com.simple.invoice.utils

import android.content.Context
import android.util.Patterns
import com.simple.invoice.R

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

}