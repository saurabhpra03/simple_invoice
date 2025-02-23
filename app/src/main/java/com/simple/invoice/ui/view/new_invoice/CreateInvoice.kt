package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.google.gson.Gson
import com.simple.invoice.R
import com.simple.invoice.common.AlertConfirmation
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.LightGrey
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoice(navController: NavController) {

    val context = LocalContext.current

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
    val invoiceList = remember { mutableStateListOf<InvoiceItem>() }

    var showItemDeleteAlert by remember { mutableStateOf(false) }
    var deleteItem by remember { mutableStateOf<InvoiceItem?>(null) }

    if (showItemDeleteAlert) {
        AlertConfirmation(
            onDismiss = { showItemDeleteAlert = false },
            onConfirm = {
                showItemDeleteAlert = false
                deleteItem?.let {
                    invoiceList.remove(it)
                    deleteItem = null
                }}
        )
    }


    fun updateTotalAmount() {
        totalAmount = if (unitPrice.trim().isNotEmpty() && unitPrice.toDouble() > 0) {
            Constants.calculateItemTotalAmount(selectedQty, unitPrice.toDouble())
        } else {
            "0.00"
        }

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        "Add Invoice Items",
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

            val (refTopBarDivider, refFieldItem, refDropDownQty, refFieldUnitPrice, refTxtTotalAmount, refBtnAdd, refItems, refBtnNext) = createRefs()

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

            AppField(
                modifier = Modifier
                    .constrainAs(refFieldItem) {
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(parent.top, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
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
                        start.linkTo(parent.start, Dimen.dimen30)
                        top.linkTo(refFieldItem.bottom, Dimen.dimen13)
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
                        top.linkTo(refFieldItem.bottom, Dimen.dimen13)
                        end.linkTo(parent.end, Dimen.dimen30)
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
                        top.linkTo(refDropDownQty.bottom, Dimen.dimen20)
                        end.linkTo(parent.end, Dimen.dimen30)
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
                .constrainAs(refBtnAdd) {
                    start.linkTo(parent.start, Dimen.dimen30)
                    top.linkTo(refTxtTotalAmount.bottom, Dimen.dimen17)
                    end.linkTo(parent.end, Dimen.dimen30)
                    width = Dimension.fillToConstraints
                }, txt = stringResource(id = R.string.add)
            ) {


                when {
                    item.trim().isEmpty() -> itemError = context.getString(R.string.empty_item)
                    unitPrice.trim().isEmpty() || unitPrice.trim()
                        .toDouble() < 1 -> unitPriceError = context.getString(R.string.empty_price)

                    else -> {
                        // Add item to the list
                        val id =
                            if (invoiceList.size > 0) invoiceList[invoiceList.size - 1].id + 1 else 1
                        val newInvoice = InvoiceItem(
                            id = id,
                            item = item.trim(),
                            qty = selectedQty,
                            unitPrice = Constants.getValidatedNumber(unitPrice),
                            totalAmount = totalAmount
                        )
                        invoiceList.add(newInvoice)

                        subTotal = ""
                        invoiceList.forEach {
                            subTotal = if (subTotal.trim().isEmpty()) {
                                Constants.getValidatedNumber(it.totalAmount)
                            } else {
                                Constants.getValidatedNumber("${subTotal.toBigDecimal() + it.totalAmount.toBigDecimal()}")
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
                .constrainAs(refItems) {
                    start.linkTo(parent.start, Dimen.dimen30)
                    top.linkTo(refBtnAdd.bottom, Dimen.dimen15)
                    end.linkTo(parent.end, Dimen.dimen30)
                    bottom.linkTo(refBtnNext.top, Dimen.dimen20)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                list = invoiceList,
                onDelete = {
                    deleteItem = it
                    showItemDeleteAlert = true
                })

            if (invoiceList.isNotEmpty()) {
                AppButton(
                    modifier = Modifier
                        .constrainAs(refBtnNext) {
                            start.linkTo(parent.start, Dimen.dimen30)
                            end.linkTo(parent.end, Dimen.dimen30)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        },
                    txt = "${stringResource(R.string.ruppe_symbol)}$subTotal => ${stringResource(R.string.next)}"
                ) {
                    navController.navigate(
                        Screens.Home.GenerateInvoice.route
                            .replace("{sub_total}", subTotal)
                            .replace("{items}", Gson().toJson(invoiceList))
                    ) {
                        popUpTo(Screens.Home.CreateInvoice.route) {
                            inclusive = false
                        }
                    }
                }
            }


        }
    }

}

@Preview
@Composable
fun CreateInvoicePreview() {
    CreateInvoice(navController = rememberNavController())
}