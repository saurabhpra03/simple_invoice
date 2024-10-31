package com.simple.invoice.ui.view.new_invoice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.simple.invoice.R
import com.simple.invoice.common.AppButton

@Composable
fun GenerateInvoice(
    modifier: Modifier,
){
    AppButton(
        modifier = modifier,
        txt = stringResource(id = R.string.generate_invoice)
    ) {

    }
}