package com.simple.invoice.ui.view.invoices

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.simple.invoice.ui.theme.Dimen

@Composable
fun AddInvoice(
    modifier: Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier
            .width(Dimen.dimen50)
            .height(Dimen.dimen50),
        shape = RoundedCornerShape(Dimen.dimen25),
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Create invoice",
            tint = MaterialTheme.colorScheme.background
        )
    }
}