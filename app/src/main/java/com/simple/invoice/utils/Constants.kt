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

}