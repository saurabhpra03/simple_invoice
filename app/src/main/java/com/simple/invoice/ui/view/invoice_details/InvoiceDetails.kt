package com.simple.invoice.ui.view.invoice_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.simple.invoice.common.AppLoader
import com.simple.invoice.data.Resource
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Grey
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.ui.view.new_invoice.Items
import com.simple.invoice.ui.viewModel.InvoicesViewModel
import com.simple.invoice.utils.Constants.convertTimeInMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetails(
    navController: NavController,
    invoiceId: Int,
    viewModel: InvoicesViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    viewModel.fetchInvoice(invoiceId)
    val invoiceFlow = viewModel.invoice.collectAsState()

    var invoiceDate by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = invoiceDate,
                        style = TextStyle(
                            fontSize = Dimen.txt17,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back icon",
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
            val (refTopBarDivider, refName, refInvoiceNo, refDivider1, refItems,
                refTotalTopDivider, refAmountCalculation,
                refTotalBottomDivider, refTotalAmount,
                refEmpty, refLoader) = createRefs()

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(refTopBarDivider){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .height(Dimen.dimen1),
                color = LightGrey
            )

            invoiceFlow.value?.let {
                when (it) {
                    is Resource.Loading -> {
                        AppLoader(modifier = Modifier
                            .constrainAs(refLoader) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            })
                    }

                    is Resource.Success -> {

                        val invoice = it.data

                        invoiceDate = invoice.date.convertTimeInMillisToDate()


                        Text(
                            modifier = Modifier
                                .constrainAs(refName) {
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    top.linkTo(parent.top, Dimen.dimen10)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    width = Dimension.fillToConstraints
                                },
                            text = stringResource(R.string.name),
                            style = TextStyle(
                                fontSize = Dimen.txt14,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(refInvoiceNo) {
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    top.linkTo(refName.bottom)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    width = Dimension.fillToConstraints
                                },
                            text = invoice.invoiceNo,
                            style = TextStyle(
                                fontSize = Dimen.txt11,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .constrainAs(refDivider1) {
                                    start.linkTo(parent.start)
                                    top.linkTo(refInvoiceNo.bottom, Dimen.dimen10)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                },
                            thickness = Dimen.dimen1,
                            color = Grey
                        )

                        Items(modifier = Modifier
                            .constrainAs(refItems) {
                                start.linkTo(parent.start, Dimen.screenPadding)
                                top.linkTo(refDivider1.bottom, Dimen.dimen10)
                                end.linkTo(parent.end, Dimen.screenPadding)
                                bottom.linkTo(refTotalTopDivider.top, Dimen.dimen7)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                            list = Gson().fromJson(invoice.items, Array<InvoiceItem>::class.java)
                                .toMutableList(),
                            onDelete = {

                            })

                        HorizontalDivider(
                            modifier = Modifier
                                .constrainAs(refTotalTopDivider){
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(refAmountCalculation.top, Dimen.dimen13)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth(),
                            color = LightGrey,
                        )

                        SubTotal(
                            context = context,
                            modifier = Modifier
                                .constrainAs(refAmountCalculation) {
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    bottom.linkTo(refTotalBottomDivider.top, Dimen.dimen7)
                                    width = Dimension.fillToConstraints
                                },
                            gst = invoice.gst,
                            discountType = invoice.discountType,
                            discount = invoice.discount,
                            subTotal = invoice.subTotal,
                            extraCharges = invoice.extraCharges
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .constrainAs(refTotalBottomDivider) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(refTotalAmount.top, Dimen.dimen3)
                                    width = Dimension.fillToConstraints
                                },
                            color = LightGrey
                        )

                        TotalAmount(
                            modifier = Modifier
                                .constrainAs(refTotalAmount) {
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    bottom.linkTo(parent.bottom, Dimen.screenPadding)
                                    width = Dimension.fillToConstraints
                                },
                            totalAmount = invoice.totalAmount
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


        }
    }

}

