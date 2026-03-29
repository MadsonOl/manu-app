package com.manu.manu_app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.model.OrdemServicoRequest
import com.manu.manu_app.data.repository.ChamadoRepository
import com.manu.manu_app.data.repository.OrdemRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GerarOsUiState(
    val chamado: ChamadoResponse? = null,
    val responsavel: String = "",
    val isLoading: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null
)

sealed class GerarOsEvent {
    object Sucesso : GerarOsEvent()
}

class GerarOsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val chamadoId: String = checkNotNull(savedStateHandle["chamadoId"])
    private val chamadoRepo = ChamadoRepository()
    private val ordemRepo = OrdemRepository()
    private val _uiState = MutableStateFlow(GerarOsUiState())
    val uiState: StateFlow<GerarOsUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<GerarOsEvent>()
    val events: SharedFlow<GerarOsEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            chamadoRepo.buscarChamado(chamadoId).onSuccess { chamado ->
                _uiState.update { it.copy(chamado = chamado) }
            }
        }
    }

    fun onResponsavelChange(valor: String) {
        _uiState.update { it.copy(responsavel = valor) }
    }

    fun criarOrdem() {
        val chamado = _uiState.value.chamado ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val request = OrdemServicoRequest(
                local = chamado.local,
                descricao = chamado.descricao,
                prioridade = chamado.prioridade,
                solicitante = chamado.solicitante,
                profissional = _uiState.value.responsavel.ifBlank { null },
                chamado_id = chamado.id
            )
            ordemRepo.criarOrdem(request).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, sucesso = true) }
                    _events.emit(GerarOsEvent.Sucesso)
                },
                onFailure = { _uiState.update { it.copy(isLoading = false, erro = "Erro ao criar OS. Tente novamente.") } }
            )
        }
    }
}
