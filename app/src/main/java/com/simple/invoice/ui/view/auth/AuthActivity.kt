package com.simple.invoice.ui.view.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.ui.utils.Screens

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            SimpleInvoiceTheme(
                darkTheme = false
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp)
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Auth.Login.route){

                        composable(Screens.Auth.Login.route){
                            LoginScreen(navController = navController)
                        }

                        composable(Screens.Auth.Signup.route){
                            SignupScreen(navController = navController)
                        }

                    }

                }
            }
        }
    }
}