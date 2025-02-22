package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.ui.viewModel.GenerateInvoiceViewModel
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Constants.toast
import com.simple.invoice.utils.Log.logD
import com.simple.invoice.utils.Screens
import com.simple.invoice.utils.SharedPref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateInvoiceScreen(
    navController: NavController,
    sharedPref: SharedPref,
    viewModel: GenerateInvoiceViewModel = hiltViewModel(),
    initialSubTotal: String,
    items: MutableList<InvoiceItem>
) {
    val context = LocalContext.current
    val authId = lazy { sharedPref.getAuth()?.id ?: 0 }

    val createInvoiceFlow = viewModel.createInvoice.collectAsState()

    viewModel.subTotal = initialSubTotal
    viewModel.totalAmount = initialSubTotal

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }

    var isGSTDropDownMenuExpanded by remember { mutableStateOf(false) }
    var discountError by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text("Generate Invoice",
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
            val  (refTopBarDivider, refFieldCustomerName, refDropDownGST, refFieldExtraCharges,
                refRadioBtnDiscountType, refFieldDiscount, refTotalTopDivider, refAmountCalculation,
                refTotalBottomDivider, refTotalAmount, refBtnGenerateInvoice) = createRefs()

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

            AppField(
                modifier = Modifier
                    .constrainAs(refFieldCustomerName){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refTopBarDivider.bottom, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                value = name,
                onValueChange = {
                    nameError = ""
                    name = it
                },
                hint = stringResource(R.string.name),
                errorMsg = nameError,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .constrainAs(refDropDownGST){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refFieldCustomerName.bottom, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                expanded = isGSTDropDownMenuExpanded,
                onExpandedChange = { isGSTDropDownMenuExpanded = !isGSTDropDownMenuExpanded }) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = viewModel.selectedGST,
                    onValueChange = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.gst),
                            style = TextStyle(
                                fontSize = Dimen.fieldTxtSize,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGSTDropDownMenuExpanded)
                    },
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    expanded = isGSTDropDownMenuExpanded,
                    onDismissRequest = { isGSTDropDownMenuExpanded = false }) {
                    viewModel.gstList.forEach { gst ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = gst,
                                    style = TextStyle(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = Dimen.txt13,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            },
                            onClick = {
                                isGSTDropDownMenuExpanded = false
                                viewModel.selectedGST = gst
                                viewModel.calculateGST()
                            }
                        )

                    }
                }
            }

            AppField(
                modifier = Modifier
                    .constrainAs(refFieldExtraCharges){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refDropDownGST.bottom, Dimen.dimen25)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.preferredWrapContent
                    },
                value = viewModel.extraCharges,
                onValueChange = {
                    viewModel.extraCharges = Constants.getValidatedNumber(it.trim())
                    viewModel.calculateTotalAmount()
                },
                hint = stringResource(R.string.extra_charges),
                errorMsg = "",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )

            Row(
                modifier = Modifier
                    .constrainAs(refRadioBtnDiscountType){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refFieldExtraCharges.bottom, Dimen.dimen3)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(end = Dimen.dimen7),
                    text = stringResource(R.string.discount_type),
                    style = TextStyle(
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        fontSize = Dimen.txt13,
                        color = Black
                    )
                )

                viewModel.discountOptions.forEach { options ->
                    Row(
                        modifier = Modifier
                            .padding(end = Dimen.dimen7),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = viewModel.selectedDiscountOption == options,
                            onClick = {
                                viewModel.selectedDiscountOption = options
                                viewModel.discount = ""
                                viewModel.discountAmount = "0.00"
                            }
                        )

                        Text(
                            text = stringResource(options),
                            style = TextStyle(
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                fontSize = Dimen.txt13,
                                color = if(viewModel.selectedDiscountOption == options) MaterialTheme.colorScheme.primary else Black
                            )
                        )
                    }
                }
            }

            if (viewModel.selectedDiscountOption != viewModel.discountOptions[0]){
                val discountSymbol = if(viewModel.selectedDiscountOption == viewModel.discountOptions[1]) stringResource(R.string.ruppe_symbol) else stringResource(R.string.percentage_symbol)
                AppField(
                    modifier = Modifier
                        .constrainAs(refFieldDiscount){
                            start.linkTo(parent.start, Dimen.dimen30)
                            top.linkTo(refRadioBtnDiscountType.bottom)
                            end.linkTo(parent.end, Dimen.dimen30)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    value = viewModel.discount,
                    onValueChange = {
                        discountError = ""
                        if (it.trim().isNotEmpty()){
                            val validInput = Constants.getValidatedNumber(it)

                            viewModel.discount = if (discountSymbol == "%" && validInput.toDouble() in 0.0..100.0){
                                validInput
                            }else{
                                validInput
                            }
                            viewModel.calculateDiscount()
                        }
                    },
                    hint = stringResource(R.string.discount),
                    errorMsg = discountError,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(refTotalTopDivider){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(if (viewModel.selectedDiscountOption != viewModel.discountOptions[0]) refFieldDiscount.bottom else refRadioBtnDiscountType.bottom, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                color = LightGrey,
            )


            SubTotals(
                modifier = Modifier
                    .constrainAs(refAmountCalculation){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refTotalTopDivider.bottom, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                viewModel = viewModel
            )

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(refTotalBottomDivider){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refAmountCalculation.bottom, Dimen.dimen7)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                color = LightGrey
            )

            TxtTotalAmount(
                modifier = Modifier
                    .constrainAs(refTotalAmount){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refTotalBottomDivider.bottom, Dimen.dimen3)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                totalAmount = viewModel.totalAmount
            )

            AppButton(
                modifier = Modifier
                    .constrainAs(refBtnGenerateInvoice){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refTotalAmount.bottom, Dimen.dimen17)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                txt = stringResource(R.string.generate_invoice)
            ) {

                    when{
                        name.isEmpty() -> {
                            nameError = context.getString(R.string.empty_name)
                        }

                        viewModel.selectedDiscountOption != viewModel.discountOptions[0] && viewModel.discount.isEmpty() -> {
                            discountError = context.getString(R.string.empty_discount)
                        }

                        else -> {
                            val timeInMillis = System.currentTimeMillis()

                            val invoice = InvoiceEntity(
                                id = 0,
                                authId = authId.value,
                                invoiceNo = "#INV$timeInMillis${(1000..9999).random()}",
                                date = timeInMillis,
                                items = Gson().toJson(items),
                                name = name,
                                subTotal = viewModel.subTotal,
                                gst = viewModel.selectedGST,
                                extraCharges = viewModel.extraCharges,
                                discount = viewModel.discount,
                                discountType = context.getString(viewModel.selectedDiscountOption),
                                totalAmount = viewModel.totalAmount,
                                createdAt = timeInMillis
                            )

                            viewModel.addInvoice(invoice)

                        }
                    }

            }
        }

    }

    createInvoiceFlow.value?.let {
        when(it){
            is Resource.Loading -> {}

            is Resource.Success -> {

                viewModel.resetCreateInvoiceFlow()

                context.toast(it.data)

                navController.navigate(Screens.Home.Invoices.route){
                    popUpTo(Screens.Home.Invoices.route){
                        inclusive = true
                    }
                }

            }

            is Resource.Failed -> {
                context.toast(it.msg)
            }
        }
    }

}

@Composable
fun SubTotals(
    modifier: Modifier,
    viewModel: GenerateInvoiceViewModel
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(end = Dimen.dimen7)) {
            TxtSubTotals("${stringResource(R.string.sub_total)}:")
            TxtSubTotals(stringResource(R.string.gst) + "(${viewModel.selectedGST}):")
            TxtSubTotals("${stringResource(R.string.extra_charges)}:")
            TxtSubTotals(if (viewModel.selectedDiscountOption == viewModel.discountOptions[2]) "${stringResource(R.string.discount)}(${viewModel.discount}%):" else "${stringResource(R.string.discount)}:")
        }

        Column(horizontalAlignment = Alignment.End) {
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${viewModel.subTotal}")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${viewModel.gstAmount}")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${viewModel.extraCharges.ifEmpty { "0.00" }}")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${viewModel.discountAmount}")
        }
    }
}

@Composable
fun TxtSubTotals(txt: String){
    Text(
        text = txt,
        style = TextStyle(
            fontSize = Dimen.txt13,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
        )
    )
}

@Composable
fun TxtTotalAmount(
    modifier: Modifier,
    totalAmount: String){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${stringResource(R.string.total_amount)}:",
            style = TextStyle(
                fontSize = Dimen.txt15,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "${stringResource(R.string.ruppe_symbol)}$totalAmount",
            style = TextStyle(
                fontSize = Dimen.txt15,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}