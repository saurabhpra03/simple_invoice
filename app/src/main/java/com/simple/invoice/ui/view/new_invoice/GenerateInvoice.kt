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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.simple.invoice.R
import com.simple.invoice.common.AppField
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.ui.viewModel.GenerateInvoiceViewModel
import com.simple.invoice.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateInvoiceScreen(
    navController: NavController,
    viewModel: GenerateInvoiceViewModel = hiltViewModel(),
    initialSubTotal: String,
) {
    viewModel.subTotal = initialSubTotal

    var isGSTDropDownMenuExpanded by remember { mutableStateOf(false) }
    val discountError = remember { mutableStateOf("") }

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
                refRadioBtnDiscountType, refFieldDiscount, refTotalTopDivider, refBtnGenerateInvoice) = createRefs()

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
                        top.linkTo(refTopBarDivider.bottom, Dimen.dimen17)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    },
                value = viewModel.name,
                onValueChange = {
                    viewModel.name = it
                    viewModel.nameError = ""
                },
                hint = stringResource(R.string.name),
                errorMsg = viewModel.nameError,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .constrainAs(refDropDownGST){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refFieldCustomerName.bottom, Dimen.dimen17)
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
                        top.linkTo(refFieldCustomerName.bottom, Dimen.dimen17)
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
                        top.linkTo(refFieldExtraCharges.bottom, Dimen.dimen17)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = Dimen.dimen30, top = Dimen.dimen7, end = Dimen.dimen30)
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
                    errorMsg = discountError.value,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(refTotalTopDivider){
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(if (viewModel.selectedDiscountOption != viewModel.discountOptions[0]) refFieldDiscount.bottom else refRadioBtnDiscountType.bottom, Dimen.dimen17)
                        end.linkTo(parent.end, Dimen.dimen30)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
                color = LightGrey,
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimen.dimen1),
                color = LightGrey
            )

            AppField(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
                value = viewModel.name,
                onValueChange = {
                    viewModel.name = it
                    viewModel.nameError = ""
                },
                hint = stringResource(R.string.name),
                errorMsg = viewModel.nameError,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
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
                    .padding(start = Dimen.dimen30, top = Dimen.dimen30, end = Dimen.dimen30)
                    .fillMaxWidth(),
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
                    .padding(start = Dimen.dimen30, top = Dimen.dimen7, end = Dimen.dimen30)
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
                        .padding(horizontal = Dimen.dimen30)
                        .fillMaxWidth(),
                    value = viewModel.discount,
                    onValueChange = {
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
                    errorMsg = discountError.value,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
                color = LightGrey,
            )

            Row(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(end = Dimen.dimen7)) {
                    Text(
                        text = "${stringResource(R.string.sub_total)}:",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = stringResource(R.string.gst) + "(${viewModel.selectedGST}):",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.extra_charges)}:",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = if (viewModel.selectedDiscountOption == viewModel.discountOptions[2]) "${stringResource(R.string.discount)}(${viewModel.discount}%):" else "${stringResource(R.string.discount)}:",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${viewModel.subTotal}",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${viewModel.gstAmount}",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${viewModel.extraCharges.ifEmpty { "0.00" }}",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${viewModel.discountAmount}",
                        style = TextStyle(
                            fontSize = Dimen.txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen17, end = Dimen.dimen30)
                    .fillMaxWidth(),
                color = LightGrey
            )

            Row(
                modifier = Modifier
                    .padding(start = Dimen.dimen30, top = Dimen.dimen3, end = Dimen.dimen30)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.total_amount)}:",
                    style = TextStyle(
                        fontSize = Dimen.txt17,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "${stringResource(R.string.ruppe_symbol)}${viewModel.totalAmount}",
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