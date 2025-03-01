package com.simple.invoice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.data.Resource
import com.simple.invoice.data.db.entity.AuthEntity
import com.simple.invoice.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signUpFlow = MutableStateFlow<Resource<AuthEntity>?>(null)
    val signUpFlow: StateFlow<Resource<AuthEntity>?> get() = _signUpFlow

    private val _loginFlow = MutableStateFlow<Resource<AuthEntity>?>(null)
    val loginFlow: StateFlow<Resource<AuthEntity>?> get() = _loginFlow

    fun signUp(auth: AuthEntity) = viewModelScope.launch {
        _signUpFlow.value = Resource.Loading
        _signUpFlow.value = repository.addUser(auth)
    }

    fun login(emailId: String, password: String) =
        viewModelScope.launch {
            _loginFlow.value = Resource.Loading
            _loginFlow.value = repository.login(emailId, password)
        }

    fun resetFlow() {
        _signUpFlow.value = null
        _loginFlow.value = null

    }

}