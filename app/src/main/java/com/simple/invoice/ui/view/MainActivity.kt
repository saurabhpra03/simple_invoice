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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.ui.theme.SimpleInvoiceTheme
import com.simple.invoice.utils.Screens
import com.simple.invoice.ui.view.invoice.CreateInvoice
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
                        .padding(30.dp)) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Home.CreateInvoice.route,
                        modifier = Modifier.padding(innerPadding)){

                        composable(Screens.Home.CreateInvoice.route){
                            CreateInvoice(navController = navController)
                        }

                    }

                }
            }
        }
    }
}