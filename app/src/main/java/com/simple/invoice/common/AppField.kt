package com.simple.invoice.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Green

@Composable
fun AppField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    isError: Boolean,
    errorMsg: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false
) {

    var showPassword by remember { mutableStateOf(false) }

    Column {

        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = hint,
                    style = TextStyle(
                        fontSize = Dimen.fieldTxtSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                autoCorrect = false,
                imeAction = imeAction
            ),
            trailingIcon = {
                if (value.isNotEmpty() && isPassword) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password visibility",
                            tint = Green
                        )
                    }
                }
            },
            visualTransformation = when {
                isPassword && !showPassword -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = Dimen.fieldTxtSize,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal
            )

        )

        Text(
            modifier = Modifier
                .padding(top = Dimen.dimen3),
            text = errorMsg,
            style = TextStyle(
                fontSize = Dimen.errorTxtSize,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.error
            )
        )
    }

}