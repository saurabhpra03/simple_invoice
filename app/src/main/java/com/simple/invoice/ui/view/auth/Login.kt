package com.simple.invoice.ui.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.common.ButtonSpace
import com.simple.invoice.common.FieldSpace
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.utils.Screens
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Validator

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current

    var emailId by remember { mutableStateOf("") }
    var emailIdError by remember { mutableStateOf("") }
    var isEmailIdError by remember { mutableStateOf(false) }
    val emailIdUpdate = { data: String ->
        emailId = data
        emailIdError = Validator.isValidEmailId(context, emailId.trim())
        isEmailIdError = emailIdError.isNotEmpty()
    }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    val passwordUpdate = { data: String ->
        password = data
        passwordError = Validator.isValidPassword(context, password)
        isPasswordError = passwordError.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppField(
            value = emailId,
            onValueChange = emailIdUpdate,
            hint = stringResource(id = R.string.email_id),
            isError = isEmailIdError,
            errorMsg = emailIdError,
            keyboardType = KeyboardType.Email
        )

        FieldSpace()

        AppField(
            value = password,
            onValueChange = passwordUpdate,
            hint = stringResource(id = R.string.password),
            isError = isPasswordError,
            errorMsg = passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isPassword = true
        )

        ButtonSpace()

        AppButton(txt = stringResource(id = R.string.login)) {

        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier
                .clickable {
                    Constants.finishAndGotoNextScreen(
                        navController = navController,
                        route = Screens.Auth.Signup.route
                    )
                },
            text = stringResource(id = R.string.no_account_sign_up),
            style = TextStyle(
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                color = Black
            ),
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}