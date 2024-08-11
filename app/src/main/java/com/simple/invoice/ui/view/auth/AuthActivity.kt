package com.simple.invoice.ui.view.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.ui.utils.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            SimpleInvoiceTheme(
                darkTheme = false,
            ) {

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(horizontal = 30.dp)
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Auth.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(Screens.Auth.Login.route) {
                            LoginScreen(navController = navController)
                        }

                        composable(Screens.Auth.Signup.route) {
                            SignupScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}