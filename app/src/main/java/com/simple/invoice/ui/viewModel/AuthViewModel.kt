package com.simple.invoice.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.AuthEntity
import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import com.simple.invoice.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _signUpFlow = MutableStateFlow<Resource<AuthEntity>?>(null)
    val signUpFlow: StateFlow<Resource<AuthEntity>?> get() = _signUpFlow

    private val _loginFlow = MutableStateFlow<Resource<AuthEntity>?>(null)
    val loginFlow: StateFlow<Resource<AuthEntity>?> get() = _loginFlow

    fun signUp(auth: AuthEntity) {
        _signUpFlow.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val result = repository.addUser(auth)
                withContext(coroutineDispatcherProvider.Main()) {
                    if (result > 0) {
                        auth.id = result.toInt()
                        _signUpFlow.value = Resource.Success(auth)
                    } else {
                        _signUpFlow.value =
                            Resource.Failed(context.getString(R.string.email_id_or_account_already_exists))
                    }
                }
            } catch (e: Exception) {
                withContext(coroutineDispatcherProvider.Main()) {
                    _signUpFlow.value = Resource.Failed(
                        e.message ?: context.getString(R.string.error_occurred_try_again)
                    )
                }
            }
        }
    }

    fun login(emailId: String, password: String) {
        _loginFlow.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response: AuthEntity? = repository.login(emailId, password)
                response?.let {
                    _loginFlow.value = Resource.Success(it)
                } ?: run {
                    _loginFlow.value = Resource.Failed(context.getString(R.string.login_failed))
                }
            } catch (e: Exception) {
                _loginFlow.value = Resource.Failed(
                    e.message ?: context.getString(R.string.error_occurred_try_again)
                )
            }
        }
    }

    fun clearSignUpFlow() {
        _signUpFlow.value = null

    }

    fun clearSignInFlow() {
        _loginFlow.value = null
    }

}