package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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

    // List of available GST rates and selected GST state
    val gstList = listOf("0%", "5%", "12%", "18%", "28%")
    var selectedGST by remember { mutableStateOf(gstList[0]) }
    var isGSTDropDownMenuExpanded by remember { mutableStateOf(false) }

    // Computed total amount for the item
    var totalAmount by remember { mutableStateOf("0.00") }

    // List to hold the current invoice items
    val invoiceList = remember { mutableStateListOf<InvoiceDto>() }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet = remember { mutableStateOf(false) }

    fun updateTotalAmount(){
        totalAmount = if (unitPrice.trim().isNotEmpty() && unitPrice.toDouble() > 0){
            Constants.calculateTotalAmount(selectedQty,unitPrice.toDouble(), selectedGST.replace("%", "").toInt())
        }else{
            "0.00"
        }

    }

    if (showBottomSheet.value){
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false  },
            sheetState = sheetState
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (refFieldName, refDate, refTxtSubTotal, refFieldExtraCharges, refFieldDiscount) = createRefs()
            }
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (refFieldItem, refDropDownQty, refFieldUnitPrice, refDropDownGST, refTxtTotalAmount, refBtnAdd, refItems, refBtnGenerate) = createRefs()

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
                    start.linkTo(refDropDownQty.end, Dimen.dimen15)
                    top.linkTo(refFieldItem.bottom, Dimen.dimen17)
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

        ExposedDropdownMenuBox(
            modifier = Modifier
                .constrainAs(refDropDownGST) {
                    start.linkTo(parent.start, dimen30)
                    top.linkTo(refDropDownQty.bottom, dimen20)
                    end.linkTo(refTxtTotalAmount.start)
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

                            updateTotalAmount()
                        }
                    )

                }
            }
        }


        OutlinedTextField(
            modifier = Modifier
                .constrainAs(refTxtTotalAmount) {
                    start.linkTo(refDropDownGST.end, dimen20)
                    top.linkTo(refDropDownQty.bottom, dimen20)
                    end.linkTo(parent.end, dimen30)
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
                    val newInvoice = InvoiceDto(id = id, item = item.trim(), qty = selectedQty, unitPrice = Constants.getValidatedNumber(unitPrice), gst = selectedGST, totalAmount = Constants.getValidatedNumber(totalAmount))
                    invoiceList.add(newInvoice)

                    // Reset all fields
                    item = ""
                    selectedQty = qtyList[0]
                    unitPrice = ""
                    selectedGST = gstList[0]
                    totalAmount = "0.00"
                }
            }

        }

        Items(modifier = Modifier
            .constrainAs(refItems){
                start.linkTo(parent.start, dimen30)
                top.linkTo(refBtnAdd.bottom, dimen15)
                end.linkTo(parent.end, dimen30)
                bottom.linkTo(refBtnGenerate.top, dimen20)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            list = invoiceList)

        if (invoiceList.isNotEmpty()){
            GenerateInvoice(
                modifier = Modifier
                    .constrainAs(refBtnGenerate){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, dimen30)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints}
            )
        }


    }
}

@Preview
@Composable
fun CreateInvoicePreview() {
    CreateInvoice(navController = rememberNavController())
}