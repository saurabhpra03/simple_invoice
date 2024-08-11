package com.simple.invoice.ui.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.utils.Screens
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Validator

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }
    val nameUpdate = { data: String ->
        name = data
        nameError = when {
            name.trim().isEmpty() -> context.getString(R.string.empty_name)
            else -> ""
        }
        isNameError = nameError.isNotEmpty()
    }

    var emailId by remember { mutableStateOf("") }
    var emailIdError by remember { mutableStateOf("") }
    var isEmailIdError by remember { mutableStateOf(false) }
    val emailIdUpdate = { data: String ->
        emailId = data
        emailIdError = Validator.isValidEmailId(context, emailId)
        isEmailIdError = emailIdError.isNotEmpty()
    }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordUpdate = { data: String ->
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
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = name,
            onValueChange = nameUpdate,
            hint = stringResource(id = R.string.name),
            isError = isNameError,
            errorMsg = nameError,
            keyboardType = KeyboardType.Text
        )

        AppField(
            modifier = Modifier
                .padding(top = Dimen.dimen20)
                .fillMaxWidth()
                .wrapContentHeight(),
            value = emailId,
            onValueChange = emailIdUpdate,
            hint = stringResource(id = R.string.email_id),
            isError = isEmailIdError,
            errorMsg = emailIdError,
            keyboardType = KeyboardType.Email
        )

        AppField(
            modifier = Modifier
                .padding(top = Dimen.dimen20)
                .fillMaxWidth()
                .wrapContentHeight(),
            value = password,
            onValueChange = passwordUpdate,
            hint = stringResource(id = R.string.password),
            isError = isPasswordError,
            errorMsg = passwordError,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            imeAction = ImeAction.Done
        )

        AppButton(
            modifier = Modifier
                .padding(Dimen.dimen30)
                .fillMaxWidth()
                .height(Dimen.buttonHeight),
            txt = stringResource(id = R.string.sign_up)) {

        }

        Text(
            modifier = Modifier
                .padding(top = Dimen.dimen30)
                .clickable {
                    Constants.finishAndGotoNextScreen(
                        navController = navController,
                        route = Screens.Auth.Login.route
                    )
                },
            text = stringResource(id = R.string.already_account_login),
            style = TextStyle(
                fontSize = Dimen.txt13,
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
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}