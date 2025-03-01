package com.simple.invoice.ui.view.invoice_details

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.simple.invoice.R
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.utils.Constants

@Composable
fun SubTotal(
    context: Context,
    modifier: Modifier,
    gst: String,
    discountType: String,
    discount: String,
    subTotal: String,
    extraCharges: String
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(end = Dimen.dimen7)) {
            TxtSubTotals("${stringResource(R.string.sub_total)}:")
            TxtSubTotals(stringResource(R.string.gst) + "(${gst}):")
            TxtSubTotals("${stringResource(R.string.extra_charges)}:")
            TxtSubTotals(
                if (discountType == context.getString(R.string.percentage_symbol)) "${
                    stringResource(R.string.discount)}(${discount}%):" else "${stringResource(R.string.discount)}:"
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}$subTotal")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${Constants.calculateGST(gst,subTotal)}")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${extraCharges.ifEmpty { "0.00" }}")
            TxtSubTotals("${stringResource(R.string.ruppe_symbol)}${Constants.calculateDiscount(context, discountType, discount, subTotal)}")
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