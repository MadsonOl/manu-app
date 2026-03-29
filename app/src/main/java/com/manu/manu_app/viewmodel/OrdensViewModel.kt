package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.OrdemServicoResponse
import com.manu.manu_app.data.repository.OrdemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrdensUiState(
    val ordens: List<OrdemServicoResponse> = emptyList(),
    val isLoading: Boolean = false,
    val erro: String? = null
)

class OrdensViewModel : ViewModel() {

    private val repository = OrdemRepository()
    private val _uiState = MutableStateFlow(OrdensUiState())
    val uiState: StateFlow<OrdensUiState> = _uiState.asStateFlow()

    init {
        carregarOrdens()
    }

    fun carregarOrdens() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            repository.listarOrdens().fold(
                onSuccess = { lista -> _uiState.update { it.copy(isLoading = false, ordens = lista) } },
                onFailure = { _uiState.update { it.copy(isLoading = false, erro = "Erro ao carregar ordens") } }
            )
        }
    }
}
