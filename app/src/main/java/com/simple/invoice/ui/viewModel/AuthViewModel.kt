package com.simple.invoice.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.R
import com.simple.invoice.data.Resource
import com.simple.invoice.data.model.Auth
import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import com.simple.invoice.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AuthRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _signUpFlow = MutableStateFlow<Resource?>(null)
    val signUpFlow: StateFlow<Resource?> get() = _signUpFlow

    private val _loginFlow = MutableStateFlow<Resource?>(null)
    val loginFlow: StateFlow<Resource?> get() = _loginFlow

    fun isUserExists(auth: Auth){
        _signUpFlow.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response: Int = repository.isUserExists(auth.emailId)
                if (response > 0){
                    _signUpFlow.value = Resource.Failed(context.getString(R.string.user_already_exists))
                }else{
                    addUser(auth)
                }
            }catch (e: Exception){
                _signUpFlow.value = Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }
    }

    private fun addUser(auth: Auth) {
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val result = repository.addUser(auth)
                if (result > 0){
                    _signUpFlow.value = Resource.Success(context.getString(R.string.successfully_registered))
                }else{
                    _signUpFlow.value = Resource.Failed(context.getString(R.string.registration_failed))
                }
            }catch (e: Exception){
                _signUpFlow.value = Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }
    }

    fun login(emailId: String, password: String){
        _loginFlow.value = Resource.Loading
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response: Auth? = repository.login(emailId, password)
                response?.let {
                    _loginFlow.value = Resource.Success(context.getString(R.string.successfully_login))
                } ?: run {
                    _loginFlow.value = Resource.Failed(context.getString(R.string.login_failed))
                }
            }catch (e: Exception){
                _loginFlow.value = Resource.Failed(e.message ?: context.getString(R.string.error_occurred_try_again))
            }
        }
    }

    fun clearAuthFlow(){
        _signUpFlow.value = null
        _loginFlow.value = null
    }

}