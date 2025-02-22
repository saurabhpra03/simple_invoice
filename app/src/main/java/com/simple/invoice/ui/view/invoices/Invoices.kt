package com.simple.invoice.ui.view.invoices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simple.invoice.R
import com.simple.invoice.common.AppLoader
import com.simple.invoice.data.Resource
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Grey
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.ui.viewModel.InvoicesViewModel
import com.simple.invoice.utils.Constants.convertTimeInMillisToDate
import com.simple.invoice.utils.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Invoices(
    navController: NavController,
    viewModel: InvoicesViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    viewModel.fetchInvoices()
    val invoicesFlow = viewModel.invoices.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(context.getString(R.string.invoices),
                        style = TextStyle(
                            fontSize = Dimen.txt17,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
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
                    .constrainAs(refTopBarDivider){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .height(Dimen.dimen1),
                color = LightGrey
            )

            invoicesFlow.value?.let {
                when(it){

                    is Resource.Loading -> {
                        AppLoader(
                            modifier = Modifier
                                .constrainAs(refLoader){
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }

                    is Resource.Success -> {

                        LazyColumn(
                            modifier = Modifier
                                .constrainAs(refInvoices){
                                    start.linkTo(parent.start, Dimen.screenPadding)
                                    top.linkTo(parent.top, Dimen.screenPadding)
                                    end.linkTo(parent.end, Dimen.screenPadding)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                }
                        ) {
                            items(it.data.size){ index ->
                                val invoice = it.data[index]

                                ElevatedCard(
                                    modifier = Modifier
                                        .padding(bottom = Dimen.dimen10)
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    shape = RoundedCornerShape(Dimen.dimen3)
                                ) {
                                    ConstraintLayout(
                                        modifier = Modifier
                                            .padding(Dimen.dimen7)
                                            .fillMaxWidth()
                                    ) {
                                        val (refDate, refCustomerName, refInvoiceNo, refTotalAmount) = createRefs()

                                        Text(
                                            modifier = Modifier
                                                .constrainAs(refDate){
                                                    top.linkTo(parent.top)
                                                    end.linkTo(parent.end)
                                                },
                                            text = invoice.date.convertTimeInMillisToDate(),
                                            style = TextStyle(
                                                fontSize = Dimen.txt11,
                                                fontStyle = FontStyle.Normal,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        )

                                        Text(
                                            modifier = Modifier
                                                .constrainAs(refCustomerName){
                                                    start.linkTo(parent.start)
                                                    top.linkTo(refDate.bottom, Dimen.dimen3)
                                                    end.linkTo(parent.end)
                                                    width = Dimension.fillToConstraints
                                                },
                                            text = invoice.name,
                                            style = TextStyle(
                                                fontSize = Dimen.txt14,
                                                fontStyle = FontStyle.Normal,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onBackground
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            modifier = Modifier
                                                .constrainAs(refInvoiceNo){
                                                    start.linkTo(parent.start)
                                                    top.linkTo(refCustomerName.bottom)
                                                    end.linkTo(parent.end)
                                                    width = Dimension.fillToConstraints
                                                },
                                            text = invoice.invoiceNo,
                                            style = TextStyle(
                                                fontSize = Dimen.txt11,
                                                fontStyle = FontStyle.Normal,
                                                fontWeight = FontWeight.Normal,
                                                color = Grey
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            modifier = Modifier
                                                .constrainAs(refTotalAmount){
                                                    top.linkTo(refInvoiceNo.bottom, Dimen.dimen7)
                                                    end.linkTo(parent.end)
                                                },
                                            text = context.getString(R.string.ruppe_symbol) + invoice.totalAmount,
                                            style = TextStyle(
                                                fontSize = Dimen.txt17,
                                                fontStyle = FontStyle.Normal,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }
                                }

                            }
                        }

                    }

                    is Resource.Failed -> {
                        Text(
                            modifier = Modifier
                                .constrainAs(refEmpty){
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
                    .constrainAs(refCreateInvoice){
                        end.linkTo(parent.end, Dimen.screenPadding)
                        bottom.linkTo(parent.bottom, Dimen.screenPadding)
                    }
            ){
                navController.navigate(Screens.Home.CreateInvoice.route){
                    popUpTo(Screens.Home.Invoices.route){
                        inclusive = false
                    }
                }
            }
        }

    }

}