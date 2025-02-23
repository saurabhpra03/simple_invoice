package com.simple.invoice.ui.view.invoices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.simple.invoice.R
import com.simple.invoice.common.AlertConfirmation
import com.simple.invoice.common.AppLoader
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.ui.viewModel.InvoicesViewModel
import com.simple.invoice.utils.Constants.toast
import com.simple.invoice.utils.Screens
import com.simple.invoice.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Invoices(
    navController: NavController,
    sharedPref: SharedPref,
    viewModel: InvoicesViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var showDeleteInvoiceAlert by remember { mutableStateOf(false) }
    var invoiceEntity by remember { mutableStateOf<InvoiceEntity?>(null) }

    var showLogoutAlert by remember { mutableStateOf(false) }

    if (showDeleteInvoiceAlert) {
        AlertConfirmation(
            onDismiss = { showDeleteInvoiceAlert = false },
            onConfirm = {
                showDeleteInvoiceAlert = false
                invoiceEntity?.let {
                    viewModel.deleteInvoice(it)
                    invoiceEntity = null
                }
            }
        )
    }

    if (showLogoutAlert){
        AlertConfirmation(
            onDismiss = { showLogoutAlert = false },
            onConfirm = {
                showDeleteInvoiceAlert = false
                sharedPref.logout()
            }
        )
    }

    viewModel.fetchInvoices()
    val invoicesFlow = viewModel.invoices.collectAsState()
    val invoiceDeleteFlow = viewModel.deleteInvoice.collectAsState()

    invoiceDeleteFlow.value?.let {
        when(it){
            is Resource.Loading -> {}

            is Resource.Success -> {
                context.toast(it.data)
                viewModel.resetInvoiceDeleteFlow()
                viewModel.fetchInvoices()
            }

            is Resource.Failed -> {
                context.toast(it.msg)
                viewModel.resetInvoiceDeleteFlow()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        context.getString(R.string.invoices),
                        style = TextStyle(
                            fontSize = Dimen.txt17,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showLogoutAlert = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->


        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val (refTopBarDivider, refLoader, refInvoices, refEmpty, refCreateInvoice) = createRefs()


            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(refTopBarDivider) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .height(Dimen.dimen1),
                color = LightGrey
            )

            invoicesFlow.value?.let {
                when (it) {

                    is Resource.Loading -> {
                        AppLoader(
                            modifier = Modifier
                                .constrainAs(refLoader) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }

                    is Resource.Success -> {

                        InvoiceItems(
                            modifier = Modifier
                                .constrainAs(refInvoices) {
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    top.linkTo(parent.top, Dimen.screenPadding)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            list = it.data,
                            onClick = { invoice ->
                                navController.navigate(Screens.Home.InvoiceDetails.route
                                    .replace("{id}", "${invoice.id}")){
                                    popUpTo(Screens.Home.Invoices.route){
                                        inclusive = false
                                    }
                                }
                            },
                            onDelete = { invoice ->
                                showDeleteInvoiceAlert = true
                                invoiceEntity = invoice
                            }
                        )

                    }

                    is Resource.Failed -> {
                        Text(
                            modifier = Modifier
                                .constrainAs(refEmpty) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            text = stringResource(R.string.no_invoice_found),
                            style = TextStyle(
                                fontSize = Dimen.txt15,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.SemiBold,
                                color = LightGrey
                            )
                        )
                    }

                }
            }

            AddInvoice(
                modifier = Modifier
                    .constrainAs(refCreateInvoice) {
                        end.linkTo(parent.end, Dimen.screenPadding)
                        bottom.linkTo(parent.bottom, Dimen.screenPadding)
                    }
            ) {
                navController.navigate(Screens.Home.CreateInvoice.route) {
                    popUpTo(Screens.Home.Invoices.route) {
                        inclusive = false
                    }
                }
            }
        }

    }

}