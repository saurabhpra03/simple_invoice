package com.simple.invoice.ui.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import com.simple.invoice.data.model.Auth
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.utils.Screens
import com.simple.invoice.ui.view.MainActivity
import com.simple.invoice.ui.viewModel.AuthViewModel
import com.simple.invoice.utils.Constants.finishAndGotoNextActivity
import com.simple.invoice.utils.Constants.finishAndGotoNextScreen
import com.simple.invoice.utils.Constants.toast
import com.simple.invoice.utils.SharedPref
import com.simple.invoice.utils.Validator

@Composable
fun SignupScreen(
    navController: NavController,
    sharedPref: SharedPref,
    viewModel: AuthViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val signUpFlow = viewModel.signUpFlow.collectAsState()

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }

    var emailId by remember { mutableStateOf("") }
    var emailIdError by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (refFieldName, refFieldEmailID, refFieldPassword, refBtnSignUp, refTxtSignIn, refLoader) = createRefs()


        AppField(
            modifier = Modifier
                .constrainAs(refFieldName){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(refFieldEmailID.top, Dimen.dimen20)
                    width = Dimension.fillToConstraints
                },
            value = name,
            onValueChange = {
                name = it
                nameError = ""
            },
            hint = stringResource(id = R.string.name),
            errorMsg = nameError,
            keyboardType = KeyboardType.Text
        )

        AppField(
            modifier = Modifier
                .constrainAs(refFieldEmailID){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(refFieldPassword.top, Dimen.dimen20)
                    width = Dimension.fillToConstraints
                },
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
                .constrainAs(refFieldPassword){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
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
                .constrainAs(refBtnSignUp){
                    start.linkTo(parent.start)
                    top.linkTo(refFieldPassword.bottom, Dimen.dimen30)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }, txt = stringResource(id = R.string.sign_up)) {

            val email = Validator.isValidEmailId(context, emailId)
            val pwd = Validator.isValidPassword(context, password)

            when{
                name.trim().isEmpty() -> nameError = context.getString(R.string.empty_name)
                email.isNotEmpty() -> emailIdError = email
                pwd.isNotEmpty() -> passwordError = pwd
                else -> viewModel.signUp(Auth(name = name.trim(), emailId = emailId.trim(), password = password.trim()))
            }
        }

        Text(
            modifier = Modifier
                .constrainAs(refTxtSignIn){
                    start.linkTo(parent.start)
                    top.linkTo(refBtnSignUp.bottom, Dimen.dimen30)
                    end.linkTo(parent.end)
                }
                .clickable {
                    navController.finishAndGotoNextScreen(
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


        signUpFlow.value?.let { response ->
            when(response){
                is Resource.Success -> {
                    sharedPref.saveAuth(response.data)
                    context.finishAndGotoNextActivity(MainActivity::class.java)
                }
                is Resource.Failed -> {
                    context.toast(response.msg)
                }
                is Resource.Loading -> {
                    AppLoader(modifier = Modifier
                        .constrainAs(refLoader){
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        })
                }
            }
            viewModel.clearSignUpFlow()
        }

    }
}