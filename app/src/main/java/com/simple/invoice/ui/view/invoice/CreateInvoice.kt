package com.simple.invoice.ui.view.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.ui.theme.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoice(navController: NavController) {

    val context = LocalContext.current

    var item by remember { mutableStateOf("") }
    var itemError by remember { mutableStateOf("") }

    val qtyList = (1..100).toMutableList()
    var selectedQty by remember { mutableIntStateOf(qtyList[0]) }
    var isQtyDropDownMenuExpanded by remember { mutableStateOf(false) }

    var unitPrice by remember { mutableStateOf("") }
    var unitPriceError by remember { mutableStateOf("") }

    val gstList = listOf("0%", "5%", "12%", "18%", "28%")
    var selectedGST by remember { mutableStateOf(gstList[0]) }
    var isGSTDropDownMenuExpanded by remember { mutableStateOf(false) }

    var totalAmount by remember { mutableStateOf("0.00") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (refFieldItem, refDropDownQty, refDropDownGST, refFieldUnitPrice, refTxtTotalAmount, refBtnAdd) = createRefs()

        AppField(
            modifier = Modifier
                .constrainAs(refFieldItem) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            value = item,
            onValueChange = {
                item = it
                itemError = ""
            },
            hint = stringResource(id = R.string.description_of_item),
            errorMsg = itemError,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .constrainAs(refDropDownQty) {
                    start.linkTo(parent.start)
                    top.linkTo(refFieldItem.bottom, Dimen.dimen17)
                    end.linkTo(refDropDownGST.start)
                    width = Dimension.fillToConstraints
                },
            expanded = isQtyDropDownMenuExpanded,
            onExpandedChange = { isQtyDropDownMenuExpanded = !isQtyDropDownMenuExpanded }) {

            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = "$selectedQty",
                onValueChange = {},
                label = {
                    Text(
                        text = stringResource(id = R.string.qty),
                        style = TextStyle(
                            fontSize = Dimen.fieldTxtSize,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Normal
                        )
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isQtyDropDownMenuExpanded)
                },
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                expanded = isQtyDropDownMenuExpanded,
                onDismissRequest = { isQtyDropDownMenuExpanded = false }) {
                qtyList.forEach { qty ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$qty",
                                style = TextStyle(
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = Dimen.txt13,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        },
                        onClick = {
                            isQtyDropDownMenuExpanded = false
                            selectedQty = qty
                        }
                    )

                }
            }

        }

        ExposedDropdownMenuBox(
            modifier = Modifier
                .constrainAs(refDropDownGST) {
                    start.linkTo(refDropDownQty.end, Dimen.dimen20)
                    top.linkTo(refFieldItem.bottom, Dimen.dimen17)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
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
                        }
                    )

                }
            }
        }

        AppField(
            modifier = Modifier
                .constrainAs(refFieldUnitPrice) {
                    start.linkTo(parent.start)
                    top.linkTo(refDropDownQty.bottom, Dimen.dimen20)
                    end.linkTo(refTxtTotalAmount.start)
                    width = Dimension.fillToConstraints
                },
            value = unitPrice,
            onValueChange = {
                unitPrice = it
                unitPriceError = ""
            },
            hint = stringResource(id = R.string.price),
            errorMsg = unitPriceError,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )


        OutlinedTextField(
            modifier = Modifier
                .constrainAs(refTxtTotalAmount) {
                    start.linkTo(refFieldUnitPrice.end, Dimen.dimen20)
                    top.linkTo(refDropDownQty.bottom, Dimen.dimen20)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            value = "${stringResource(id = R.string.ruppe_symbol)}$totalAmount",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(id = R.string.total_amount),
                    style = TextStyle(
                        fontSize = Dimen.fieldTxtSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = Dimen.fieldTxtSize,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal
            )
        )

        AppButton(modifier = Modifier
            .constrainAs(refBtnAdd){
                start.linkTo(parent.start)
                top.linkTo(refFieldUnitPrice.bottom, Dimen.dimen20)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, txt = stringResource(id = R.string.add)) {


            when{
                item.trim().isEmpty() -> itemError = context.getString(R.string.empty_item)
                unitPrice.trim().isEmpty() -> unitPriceError = context.getString(R.string.empty_price)
                else -> {}
            }

        }
    }
}

@Preview
@Composable
fun CreateInvoicePreview() {
    CreateInvoice(navController = rememberNavController())
}