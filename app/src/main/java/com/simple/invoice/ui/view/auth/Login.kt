package com.simple.invoice.ui.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simple.invoice.R
import com.simple.invoice.common.AppButton
import com.simple.invoice.common.AppField
import com.simple.invoice.common.AppLoader
import com.simple.invoice.data.Resource
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.utils.Screens
import com.simple.invoice.ui.view.MainActivity
import com.simple.invoice.ui.viewModel.AuthViewModel
import com.simple.invoice.utils.Constants.finishAndGotoNextActivity
import com.simple.invoice.utils.Constants.finishAndGotoNextScreen
import com.simple.invoice.utils.Constants.toast
import com.simple.invoice.utils.SharedPref
import com.simple.invoice.utils.Validator

@Composable
fun LoginScreen(
    navController: NavController,
    sharedPref: SharedPref,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val loginFlow = viewModel.loginFlow.collectAsState()

    var emailId by remember { mutableStateOf("") }
    var emailIdError by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (refFieldEmail, refFieldPassword, refBtnLogin, refTxtRegistration, refLoader) = createRefs()

        AppField(
            modifier = Modifier
                .constrainAs(refFieldEmail) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(refFieldPassword.top, Dimen.dimen20)
                    width = Dimension.fillToConstraints
                }
                .wrapContentHeight(),
            value = emailId,
            onValueChange = {
                emailId = it
                emailIdError = ""
            },
            hint = stringResource(id = R.string.email_id),
            errorMsg = emailIdError,
            keyboardType = KeyboardType.Email
        )

        AppField(
            modifier = Modifier
                .constrainAs(refFieldPassword) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .wrapContentHeight(),
            value = password,
            onValueChange = {
                password = it
                passwordError = ""
            },
            hint = stringResource(id = R.string.password),
            errorMsg = passwordError,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isPassword = true
        )

        AppButton(
            modifier = Modifier
                .padding(top = Dimen.dimen30)
                .constrainAs(refBtnLogin) {
                    start.linkTo(parent.start)
                    top.linkTo(refFieldPassword.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .height(Dimen.buttonHeight),
            txt = stringResource(id = R.string.login)
        ) {
            val email = Validator.isValidEmailId(context, emailId)
            val pwd = Validator.isValidPassword(context, password)

            when {
                email.isNotEmpty() -> emailIdError = email
                pwd.isNotEmpty() -> passwordError = pwd
                else -> viewModel.login(emailId = emailId.trim(), password = password.trim())
            }

        }

        Text(
            modifier = Modifier
                .padding(top = Dimen.dimen30)
                .constrainAs(refTxtRegistration) {
                    start.linkTo(parent.start)
                    top.linkTo(refBtnLogin.bottom)
                    end.linkTo(parent.end)
                }
                .clickable {
                    navController.finishAndGotoNextScreen(
                        route = Screens.Auth.Signup.route
                    )
                },
            text = stringResource(id = R.string.no_account_sign_up),
            style = TextStyle(
                fontSize = Dimen.txt13,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Normal,
                color = Black
            ),
            textAlign = TextAlign.Center
        )

        loginFlow.value?.let { response ->
            when (response) {
                is Resource.Success -> {
                    sharedPref.saveAuth(response.data)
                    context.finishAndGotoNextActivity(MainActivity::class.java)
                }

                is Resource.Failed -> {
                    context.toast(response.msg)
                }

                is Resource.Loading -> {
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
            viewModel.clearSignInFlow()
        }
    }

}