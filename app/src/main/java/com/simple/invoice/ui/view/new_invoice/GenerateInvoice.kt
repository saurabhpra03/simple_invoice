package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.simple.invoice.R
import com.simple.invoice.common.AppField
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Dimen.dimen17
import com.simple.invoice.ui.theme.Dimen.dimen20
import com.simple.invoice.ui.theme.Dimen.dimen3
import com.simple.invoice.ui.theme.Dimen.dimen30
import com.simple.invoice.ui.theme.Dimen.dimen7
import com.simple.invoice.ui.theme.Dimen.txt13
import com.simple.invoice.ui.theme.Dimen.txt15
import com.simple.invoice.ui.theme.Dimen.txt17
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateInvoice(
    showBottomSheet: MutableState<Boolean>,
    subTotal: String,
) {

    val sheetState = rememberModalBottomSheetState()

    var name = remember { mutableStateOf("") }
    var nameError = remember { mutableStateOf("") }

    // List of available GST rates and selected GST state
    val gstList = listOf("0%", "5%", "12%", "18%", "28%")
    var selectedGST by remember { mutableStateOf(gstList[0]) }
    var isGSTDropDownMenuExpanded by remember { mutableStateOf(false) }
    var gst by remember { mutableStateOf("0.00") }

    var extraCharges = remember { mutableStateOf("") }

    val discountOptions = listOf(stringResource(R.string.none), stringResource(R.string.ruppe_symbol), stringResource(R.string.percentage_symbol))
    val selectedDiscountOption = remember { mutableStateOf(discountOptions[0]) }
    var discount = remember { mutableStateOf("") }
    var discountAmount = remember { mutableStateOf("0.00") }

    fun calculateGST(){
        if (selectedGST != gstList[0]){
            gst = Constants.getValidatedNumber(((subTotal.toDouble() * selectedGST.replace("%", "").toInt())/100).toString())
        }
    }

    fun calculateDiscount(){
        discountAmount.value = when(selectedDiscountOption.value){
            discountOptions[0] -> "0.0"
            discountOptions[1] -> Constants.getValidatedNumber(discount.value)
            else -> {
                Constants.getValidatedNumber((subTotal.toDouble() * (discount.value.toDouble() / 100.0)).toString())
            }
        }
    }



    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = { showBottomSheet.value = false  },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(dimen30)
        ) {
            AppField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = name.value,
                onValueChange = {
                    name.value = it
                    nameError.value = ""
                },
                hint = stringResource(R.string.name),
                errorMsg = nameError.value,
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
                    value = selectedGST,
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
                    gstList.forEach { gst ->
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
                                selectedGST = gst
                                calculateGST()
                            }
                        )

                    }
                }
            }

            AppField(
                modifier = Modifier
                    .padding(top = dimen17)
                    .fillMaxWidth(),
                value = extraCharges.value,
                onValueChange = {
                    extraCharges.value = Constants.getValidatedNumber(it.trim())
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

                discountOptions.forEach { options ->
                    Row(
                        modifier = Modifier
                            .padding(end = dimen7),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = selectedDiscountOption.value == options,
                            onClick = {
                                selectedDiscountOption.value = options
                                discount.value = ""
                            }
                        )

                        Text(
                            text = options,
                            style = TextStyle(
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                fontSize = txt13,
                                color = if(selectedDiscountOption.value == options) MaterialTheme.colorScheme.primary else Black
                            )
                        )
                    }
                }
            }

            if (selectedDiscountOption.value != stringResource(R.string.none)){
                val discountSymbol = if(selectedDiscountOption.value == stringResource(R.string.ruppe_symbol)) stringResource(R.string.ruppe_symbol) else stringResource(R.string.percentage_symbol)
                AppField(
                    modifier = Modifier
                        .padding(top = dimen17)
                        .fillMaxWidth(),
                    value = discount.value,
                    onValueChange = {
                        if (it.trim().isNotEmpty()){
                            val validInput = Constants.getValidatedNumber(it)

                            discount.value = if (discountSymbol == "%" && validInput.toDouble() in 0.0..100.0){
                                validInput
                            }else{
                                validInput
                            }

                            calculateDiscount()
                        }
                    },
                    hint = stringResource(R.string.discount),
                    errorMsg = stringResource(R.string.empty_discount),
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
                        text = stringResource(R.string.gst) + if(selectedDiscountOption.value == discountOptions[1]) "($selectedGST)" else ""+":",
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
                        text = "${stringResource(R.string.discount)}:",
                        style = TextStyle(
                            fontSize = txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}$subTotal",
                        style = TextStyle(
                            fontSize = txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}$gst",
                        style = TextStyle(
                            fontSize = txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${extraCharges.value.ifEmpty { "0.00" }}",
                        style = TextStyle(
                            fontSize = txt15,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Text(
                        text = "${stringResource(R.string.ruppe_symbol)}${discountAmount.value}",
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
                    text = "${stringResource(R.string.ruppe_symbol)}$subTotal",
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
}