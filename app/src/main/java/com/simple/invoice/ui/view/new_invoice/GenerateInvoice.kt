package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simple.invoice.R
import com.simple.invoice.common.AppField
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Dimen.dimen17
import com.simple.invoice.ui.theme.Dimen.dimen3
import com.simple.invoice.ui.theme.Dimen.dimen30
import com.simple.invoice.ui.theme.Dimen.dimen7
import com.simple.invoice.ui.theme.Dimen.txt13
import com.simple.invoice.ui.theme.Dimen.txt15
import com.simple.invoice.ui.theme.Dimen.txt17
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(dimen30)
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppField(
            modifier = Modifier
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
                .padding(top = dimen17)
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
                .padding(top = dimen17)
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
                .padding(top = dimen17)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(end = dimen7),
                text = stringResource(R.string.discount_type),
                style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    fontSize = txt13,
                    color = Black
                )
            )

            viewModel.discountOptions.forEach { options ->
                Row(
                    modifier = Modifier
                        .padding(end = dimen7),
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
                            fontSize = txt13,
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
                    .padding(top = dimen17)
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
                .padding(top = dimen17)
                .fillMaxWidth(),
            color = LightGrey,
        )

        Row(
            modifier = Modifier
                .padding(top = dimen17)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(end = dimen7)) {
                Text(
                    text = "${stringResource(R.string.sub_total)}:",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = stringResource(R.string.gst) + "(${viewModel.selectedGST}):",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = "${stringResource(R.string.extra_charges)}:",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = if (viewModel.selectedDiscountOption == viewModel.discountOptions[2]) "${stringResource(R.string.discount)}(${viewModel.discount}%):" else "${stringResource(R.string.discount)}:",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${stringResource(R.string.ruppe_symbol)}${viewModel.subTotal}",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = "${stringResource(R.string.ruppe_symbol)}${viewModel.gstAmount}",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = "${stringResource(R.string.ruppe_symbol)}${viewModel.extraCharges.ifEmpty { "0.00" }}",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
                Text(
                    text = "${stringResource(R.string.ruppe_symbol)}${viewModel.discountAmount}",
                    style = TextStyle(
                        fontSize = txt15,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = dimen7)
                .fillMaxWidth(),
            color = LightGrey
        )

        Row(
            modifier = Modifier
                .padding(top = dimen3)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(R.string.total_amount)}:",
                style = TextStyle(
                    fontSize = txt17,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = "${stringResource(R.string.ruppe_symbol)}${viewModel.totalAmount}",
                style = TextStyle(
                    fontSize = txt17,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}