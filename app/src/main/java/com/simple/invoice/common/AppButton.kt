package com.simple.invoice.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    modifier: Modifier,
    txt: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick) {

        Text(
            text = txt,
            style = TextStyle(
                fontSize = 17.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal
            )
        )
    }
}