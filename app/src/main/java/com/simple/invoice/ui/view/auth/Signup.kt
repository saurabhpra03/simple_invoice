package com.simple.invoice.ui.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.common.AppLoader
import com.simple.invoice.data.Resource
import com.simple.invoice.data.model.Auth
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.utils.Screens
import com.simple.invoice.ui.view.MainActivity
import com.simple.invoice.ui.viewModel.AuthViewModel
import com.simple.invoice.utils.Constants
import com.simple.invoice.utils.Constants.finishAndGotoNextActivity
import com.simple.invoice.utils.Constants.toast
import com.simple.invoice.utils.Validator

@Composable
fun SignupScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val signUpFlow = viewModel.signUpFlow.collectAsState()

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
    val passwordUpdate = { data: String ->
        password = data
        passwordError = Validator.isValidPassword(context, password)
        isPasswordError = passwordError.isNotEmpty()
    }

    fun valid(): Boolean {
        nameUpdate(name.trim())
        emailIdUpdate(emailId.trim())
        passwordUpdate(password.trim())

        return (!isNameError || !isEmailIdError || !isPasswordError)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (refFieldName, refFieldEmail, refFieldPassword, refBtnSignup, refTxtLogin, refLoader) = createRefs()

        AppField(
            modifier = Modifier
                .constrainAs(refFieldName){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(refFieldEmail.top, Dimen.dimen20)
                }
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
                .constrainAs(refFieldEmail){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(refFieldPassword.top, Dimen.dimen20)
                    width = Dimension.fillToConstraints
                }
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
                .constrainAs(refFieldPassword){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
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
                .constrainAs(refBtnSignup){
                    start.linkTo(parent.start)
                    top.linkTo(refFieldPassword.bottom, Dimen.dimen30)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .height(Dimen.buttonHeight),
            txt = stringResource(id = R.string.sign_up)
        ) {
            if (valid()) {
                val auth = Auth(
                    id = 0,
                    name = name.trim(),
                    emailId = emailId.trim(),
                    password = password.trim()
                )
                viewModel.isUserExists(auth)
            }
        }

        Text(
            modifier = Modifier
                .constrainAs(refTxtLogin){
                    start.linkTo(parent.start)
                    top.linkTo(refBtnSignup.bottom, Dimen.dimen30)
                    end.linkTo(parent.end)
                }
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

        signUpFlow.value?.let {
            when (it) {
                is Resource.Success -> {
                    context.finishAndGotoNextActivity(MainActivity::class.java)
                }

                is Resource.Failed -> {
                    context.toast(it.msg)
                    viewModel.clearAuthFlow()
                }

                else -> {
                    AppLoader(modifier = Modifier
                        .constrainAs(refLoader) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}