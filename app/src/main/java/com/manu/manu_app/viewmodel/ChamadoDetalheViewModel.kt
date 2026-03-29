package com.manu.manu_app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.repository.ChamadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChamadoDetalheUiState(
    val chamado: ChamadoResponse? = null,
    val isLoading: Boolean = false,
    val erro: String? = null
)

class ChamadoDetalheViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val chamadoId: String = checkNotNull(savedStateHandle["chamadoId"])
    private val repository = ChamadoRepository()
    private val _uiState = MutableStateFlow(ChamadoDetalheUiState())
    val uiState: StateFlow<ChamadoDetalheUiState> = _uiState.asStateFlow()

    init {
        carregarChamado()
    }

    private fun carregarChamado() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.buscarChamado(chamadoId)
            result.fold(
                onSuccess = { chamado -> _uiState.update { it.copy(isLoading = false, chamado = chamado) } },
                onFailure = { _uiState.update { it.copy(isLoading = false, erro = "Erro ao carregar chamado") } }
            )
        }
    }
}
