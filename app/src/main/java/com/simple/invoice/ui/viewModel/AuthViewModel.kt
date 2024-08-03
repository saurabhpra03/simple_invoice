package com.simple.invoice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.invoice.data.model.Auth
import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import com.simple.invoice.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {


    fun addUser(auth: Auth) {
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                repository.addUser(auth)
            }catch (e: Exception){
                // TODO - Handle Exception
            }
        }
    }

    fun isUserExists(emailId: String){
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response: Int = repository.isUserExists(emailId)
            }catch (e: Exception){
                // TODO - Handle Exception
            }
        }
    }

    fun login(emailId: String, password: String){
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val response: Int = repository.login(emailId, password)
            }catch (e: Exception){
                // TODO - Handle Exception
            }
        }
    }

}