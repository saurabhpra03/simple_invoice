package com.simple.invoice.ui.view.new_invoice

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.simple.invoice.data.module.InvoiceDto
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Dimen.dimen15
import com.simple.invoice.ui.theme.Dimen.dimen17
import com.simple.invoice.ui.theme.Dimen.dimen20
import com.simple.invoice.ui.theme.Dimen.dimen30
import com.simple.invoice.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoice(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Item description and validation error message
    var item by remember { mutableStateOf("") }
    var itemError by remember { mutableStateOf("") }

    // List of available quantities (1-100) and selected quantity state
    val qtyList = (1..100).toMutableList()
    var selectedQty by remember { mutableIntStateOf(qtyList[0]) }
    var isQtyDropDownMenuExpanded by remember { mutableStateOf(false) }

    // Unit price for the item and validation error message
    var unitPrice by remember { mutableStateOf("") }
    var unitPriceError by remember { mutableStateOf("") }

    // Computed total amount for the item
    var totalAmount by remember { mutableStateOf("0.00") }

    var subTotal by remember { mutableStateOf("0.00") }

    // List to hold the current invoice items
    val invoiceList = remember { mutableStateListOf<InvoiceDto>() }


    val showBottomSheet = remember { mutableStateOf(false) }

    fun updateTotalAmount(){
        totalAmount = if (unitPrice.trim().isNotEmpty() && unitPrice.toDouble() > 0){
            Constants.calculateItemTotalAmount(selectedQty,unitPrice.toDouble())
        }else{
            "0.00"
        }

    }

    if (showBottomSheet.value){
        GenerateInvoice(
            showBottomSheet = showBottomSheet,
            initialSubTotal = subTotal
        )
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (refFieldItem, refDropDownQty, refFieldUnitPrice, refTxtTotalAmount, refBtnAdd, refItems, refBtnNext) = createRefs()

        AppField(
            modifier = Modifier
                .constrainAs(refFieldItem) {
                    start.linkTo(parent.start, dimen30)
                    top.linkTo(parent.top, dimen30)
                    end.linkTo(parent.end, dimen30)
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
                    start.linkTo(parent.start, dimen30)
                    top.linkTo(refFieldItem.bottom, Dimen.dimen17)
                    end.linkTo(refFieldUnitPrice.start)
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

                            updateTotalAmount()
                        }
                    )

                }
            }

        }

        AppField(
            modifier = Modifier
                .constrainAs(refFieldUnitPrice) {
                    start.linkTo(refDropDownQty.end, dimen15)
                    top.linkTo(refFieldItem.bottom, dimen17)
                    end.linkTo(parent.end, dimen30)
                    width = Dimension.fillToConstraints
                },
            value = unitPrice,
            onValueChange = {
                unitPrice = Constants.getValidatedNumber(it)
                unitPriceError = ""

                updateTotalAmount()
            },
            hint = stringResource(id = R.string.price),
            errorMsg = unitPriceError,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )

        Text(
            modifier = Modifier
                .constrainAs(refTxtTotalAmount) {
                    top.linkTo(refDropDownQty.bottom, dimen20)
                    end.linkTo(parent.end, dimen30)
                },
            text = "${stringResource(id = R.string.ruppe_symbol)}$totalAmount",
            style = TextStyle(
                fontSize = Dimen.txt17,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            ),
            maxLines = 1
        )

        AppButton(modifier = Modifier
            .constrainAs(refBtnAdd){
                start.linkTo(parent.start, dimen30)
                top.linkTo(refTxtTotalAmount.bottom, Dimen.dimen17)
                end.linkTo(parent.end, dimen30)
                width = Dimension.fillToConstraints
            }, txt = stringResource(id = R.string.add)) {


            when{
                item.trim().isEmpty() -> itemError = context.getString(R.string.empty_item)
                unitPrice.trim().isEmpty() || unitPrice.trim().toDouble() < 1 -> unitPriceError = context.getString(R.string.empty_price)
                else -> {
                    // Add item to the list
                    val id = if(invoiceList.size > 0) invoiceList[invoiceList.size - 1].id + 1 else 1
                    val newInvoice = InvoiceDto(id = id, item = item.trim(), qty = selectedQty, unitPrice = Constants.getValidatedNumber(unitPrice), totalAmount = Constants.getValidatedNumber(totalAmount))
                    invoiceList.add(newInvoice)

                    subTotal = ""
                    invoiceList.forEach {
                        subTotal = if (subTotal.trim().isEmpty()){
                            Constants.getValidatedNumber(it.totalAmount.toDouble().toString())
                        }else {
                            Constants.getValidatedNumber((subTotal.toDouble() + it.totalAmount.toDouble()).toString())
                        }
                    }

                    // Reset all fields
                    item = ""
                    selectedQty = qtyList[0]
                    unitPrice = ""
                    totalAmount = "0.00"
                }
            }

        }

        Items(modifier = Modifier
            .constrainAs(refItems){
                start.linkTo(parent.start, dimen30)
                top.linkTo(refBtnAdd.bottom, dimen15)
                end.linkTo(parent.end, dimen30)
                bottom.linkTo(refBtnNext.top, dimen20)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            list = invoiceList)

        if (invoiceList.isNotEmpty()){
            AppButton(
                modifier = Modifier
                    .constrainAs(refBtnNext){
                        start.linkTo(parent.start, dimen30)
                        end.linkTo(parent.end, dimen30)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints},
                txt = "${stringResource(R.string.ruppe_symbol)}$subTotal => ${stringResource(R.string.next)}"
            ) {
                showBottomSheet.value = true
            }
        }


    }
}

@Preview
@Composable
fun CreateInvoicePreview() {
    CreateInvoice(navController = rememberNavController())
}