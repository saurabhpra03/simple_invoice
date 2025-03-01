package com.simple.invoice.ui.view.invoice_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.simple.invoice.R
import com.simple.invoice.ui.theme.Dimen

@Composable
fun TotalAmount(
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