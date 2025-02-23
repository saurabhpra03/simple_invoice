package com.simple.invoice.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.simple.invoice.R
import com.simple.invoice.ui.theme.Dimen

@Composable
fun AlertConfirmation(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(Dimen.dimen3),
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(R.string.are_you_sure),
                style = TextStyle(
                    fontSize = Dimen.txt14,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ){
                Text(
                    text = stringResource(R.string.no),
                    style = TextStyle(
                        fontSize = Dimen.txt17,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ){
                Text(
                    text = stringResource(R.string.yes),
                    style = TextStyle(
                        fontSize = Dimen.txt17,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    )
}