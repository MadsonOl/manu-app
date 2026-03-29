package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.repository.AuthRepository
import com.manu.manu_app.data.repository.ChamadoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChamadosUiState(
    val chamados: List<ChamadoResponse> = emptyList(),
    val isLoading: Boolean = false,
    val erro: String? = null
)

sealed class ChamadosEvent {
    object Logout : ChamadosEvent()
}

class ChamadosViewModel : ViewModel() {

    private val chamadoRepo = ChamadoRepository()
    private val authRepo = AuthRepository()
    private val _uiState = MutableStateFlow(ChamadosUiState())
    val uiState: StateFlow<ChamadosUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChamadosEvent>()
    val events: SharedFlow<ChamadosEvent> = _events.asSharedFlow()

    init {
        carregarChamados()
    }

    fun carregarChamados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val result = chamadoRepo.listarChamados()
            result.fold(
                onSuccess = { lista -> _uiState.update { it.copy(isLoading = false, chamados = lista) } },
                onFailure = { _uiState.update { it.copy(isLoading = false, erro = "Erro ao carregar chamados") } }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
            _events.emit(ChamadosEvent.Logout)
        }
    }
}
