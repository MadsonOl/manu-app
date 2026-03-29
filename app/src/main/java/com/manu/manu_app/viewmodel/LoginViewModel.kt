package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class LoginEvent {
    object Success : LoginEvent()
}

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val isLoading: Boolean = false,
    val erro: String? = null
)

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onEmailChange(valor: String) {
        _uiState.update { it.copy(email = valor) }
    }

    fun onSenhaChange(valor: String) {
        _uiState.update { it.copy(senha = valor) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val result = repository.login(_uiState.value.email, _uiState.value.senha)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(LoginEvent.Success)
                },
                onFailure = { exception ->
                    val mensagem = when {
                        exception.message?.contains("user-not-found") == true -> "E-mail nao cadastrado"
                        exception.message?.contains("wrong-password") == true -> "Senha incorreta"
                        exception.message?.contains("invalid-credential") == true -> "Senha incorreta"
                        else -> "Erro ao fazer login. Tente novamente."
                    }
                    _uiState.update { it.copy(isLoading = false, erro = mensagem) }
                }
            )
        }
    }
}
