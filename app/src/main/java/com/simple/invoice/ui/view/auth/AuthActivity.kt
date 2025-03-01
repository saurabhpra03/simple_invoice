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
import com.simple.invoice.common.AppStatusBarColor
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.ui.view.MainActivity
import com.simple.invoice.utils.Constants.finishAndGotoNextActivity
import com.simple.invoice.utils.Screens
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            sharedPref.getAuth()?.let {
                this.finishAndGotoNextActivity(MainActivity::class.java)
            }

            SimpleInvoiceTheme(
                darkTheme = false,
            ) {

                AppStatusBarColor()

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
                            LoginScreen(navController = navController, sharedPref = sharedPref)
                        }

                        composable(Screens.Auth.Signup.route) {
                            SignupScreen(navController = navController, sharedPref = sharedPref)
                        }
                    }
                }
            }
        }
    }


}

