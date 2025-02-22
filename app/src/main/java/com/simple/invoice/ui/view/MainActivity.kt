package com.simple.invoice.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.ui.view.invoices.Invoices
import com.simple.invoice.ui.view.new_invoice.CreateInvoice
import com.simple.invoice.ui.view.new_invoice.GenerateInvoiceScreen
import com.simple.invoice.utils.Screens
import com.simple.invoice.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            val navController = rememberNavController()

            SimpleInvoiceTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Home.Invoices.route
                    ) {

                        composable(Screens.Home.Invoices.route) {
                            Invoices(navController)
                        }

                        composable(Screens.Home.CreateInvoice.route) {
                            CreateInvoice(navController = navController)
                        }


                        composable(Screens.Home.GenerateInvoice.route) {

                            val subTotal = it.arguments?.getString("sub_total") ?: "0.00"

                            val data = it.arguments?.getString("items")
                            val list = Gson().fromJson(data, Array<InvoiceItem>::class.java).toMutableList()

                            GenerateInvoiceScreen(
                                navController = navController,
                                sharedPref = sharedPref,
                                initialSubTotal = subTotal,
                                items = list
                            )
                        }

                    }

                }
            }
        }
    }
}