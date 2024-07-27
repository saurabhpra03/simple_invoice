package com.simple.invoice.utils

import androidx.navigation.NavController

object Constants {

    fun finishAndGotoNextScreen(navController: NavController, route: String){
        navController.popBackStack()
        navController.navigate(route){
            launchSingleTop = true
        }
    }

}