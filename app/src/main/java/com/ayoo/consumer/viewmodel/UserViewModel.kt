package com.ayoo.consumer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoo.consumer.data.UserRepository
import com.backendless.BackendlessUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserState {
    object Idle : UserState()
    object Loading : UserState()
    data class Success(val user: BackendlessUser) : UserState()
    data class Error(val message: String) : UserState()
}

sealed class SessionState {
    object Loading : SessionState()
    data class Valid(val isValid: Boolean) : SessionState()
}

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _registrationState = MutableStateFlow<UserState>(UserState.Idle)
    val registrationState: StateFlow<UserState> = _registrationState

    private val _loginState = MutableStateFlow<UserState>(UserState.Idle)
    val loginState: StateFlow<UserState> = _loginState

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _logoutState = MutableStateFlow<UserState>(UserState.Idle)
    val logoutState: StateFlow<UserState> = _logoutState

    init {
        validateSession()
    }

    fun validateSession() {
        viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            val isValid = repository.isValidLogin()
            _sessionState.value = SessionState.Valid(isValid)
        }
    }

    fun registerUser(user: BackendlessUser) {
        viewModelScope.launch {
            _registrationState.value = UserState.Loading
            try {
                val registeredUser = repository.registerUser(user)
                _loginState.value =
                    UserState.Success(registeredUser) // Also treat registration as a login
            } catch (e: Exception) {
                _registrationState.value = UserState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UserState.Loading
            try {
                val loggedInUser = repository.loginUser(email, password)
                _loginState.value = UserState.Success(loggedInUser)
            } catch (e: Exception) {
                _loginState.value = UserState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = UserState.Loading
            try {
                val loggedInUser = repository.loginWithGoogle(idToken)
                _loginState.value = UserState.Success(loggedInUser)
            } catch (e: Exception) {
                _loginState.value = UserState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            _logoutState.value = UserState.Loading
            try {
                repository.logoutUser()
                _logoutState.value =
                    UserState.Success(BackendlessUser()) // Use a dummy user on success
            } catch (e: Exception) {
                _logoutState.value = UserState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
