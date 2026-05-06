package com.example.soulstone.ui.screens.admin_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soulstone.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AdminLoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)
@HiltViewModel
class AdminLoginViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AdminLoginUiState())
    val uiState: StateFlow<AdminLoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        viewModelScope.launch {

            if (currentState.username.isBlank() || currentState.password.isBlank()) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Credentials invalid"))
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val isSuccess = performLogin(currentState.username, currentState.password)

            if (isSuccess) {
                _uiEvent.emit(UiEvent.NavigateToAdminDashboard)
                _uiState.update { AdminLoginUiState() }
            } else {
                _uiEvent.emit(UiEvent.ShowSnackbar("Invalid username or password."))
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

private suspend fun performLogin(user: String, pass: String): Boolean {
    return withContext(Dispatchers.IO) {

        delay(2000)

        return@withContext (user == "excogitatorius" && pass == "mandala2025")
    }
}