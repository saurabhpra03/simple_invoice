package com.simple.invoice.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.utils.Screens
import com.simple.invoice.ui.view.new_invoice.CreateInvoice
import com.simple.invoice.ui.view.new_invoice.GenerateInvoiceScreen
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            SimpleInvoiceTheme(darkTheme = false) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Home.CreateInvoice.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(Screens.Home.CreateInvoice.route) {
                            CreateInvoice(navController = navController)
                        }


                        composable(
                            route = Screens.Home.GenerateInvoice.route,
                            arguments = listOf(navArgument("sub_total") {
                                type = NavType.StringType
                            })
                        ) {
                            GenerateInvoiceScreen(
                                navController = navController,
                                initialSubTotal = it.arguments?.getString("sub_total") ?: "0.00"
                            )
                        }

                    }

                }
            }
        }
    }
}