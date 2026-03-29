package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.ChamadoRequest
import com.manu.manu_app.data.repository.ChamadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SolicitanteUiState(
    val local: String = "",
    val descricao: String = "",
    val prioridade: String = "NORMAL",
    val solicitante: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val gpsStatus: String = "Aguardando localizacao...",
    val isLoading: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null
)

class SolicitanteViewModel : ViewModel() {

    private val repository = ChamadoRepository()
    private val _uiState = MutableStateFlow(SolicitanteUiState())
    val uiState: StateFlow<SolicitanteUiState> = _uiState.asStateFlow()

    fun onLocalChange(valor: String) {
        _uiState.update { it.copy(local = valor) }
    }

    fun onDescricaoChange(valor: String) {
        _uiState.update { it.copy(descricao = valor) }
    }

    fun onPrioridadeChange(valor: String) {
        _uiState.update { it.copy(prioridade = valor) }
    }

    fun onSolicitanteChange(valor: String) {
        _uiState.update { it.copy(solicitante = valor) }
    }

    fun onLocalizacaoCapturada(lat: Double, lng: Double) {
        _uiState.update {
            it.copy(
                latitude = lat,
                longitude = lng,
                gpsStatus = "Localizacao capturada",
                local = if (it.local.isBlank()) "Lat: $lat, Lng: $lng" else it.local
            )
        }
    }

    fun onGpsFalhou() {
        _uiState.update { it.copy(gpsStatus = "Localizacao nao disponivel") }
    }

    fun resetSucesso() {
        _uiState.update { it.copy(sucesso = false, erro = null) }
    }

    fun enviarChamado() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val state = _uiState.value
            val request = ChamadoRequest(
                local = state.local,
                descricao = state.descricao,
                prioridade = state.prioridade,
                solicitante = state.solicitante
            )
            val result = repository.criarChamado(request)
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, sucesso = true) } },
                onFailure = { _uiState.update { s -> s.copy(isLoading = false, erro = "Erro ao enviar chamado. Tente novamente.") } }
            )
        }
    }
}
