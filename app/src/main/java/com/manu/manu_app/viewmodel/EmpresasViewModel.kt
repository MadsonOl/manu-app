package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.Empresa
import com.manu.manu_app.data.repository.EmpresaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EmpresasUiState(
    val empresas: List<Empresa> = emptyList(),
    val isLoading: Boolean = false,
    val erro: String? = null,
    val sucesso: String? = null,
    val cnpj: String = "",
    val nome: String = "",
    val endereco: String = "",
    val gestor: String = "",
    val info: String = "",
    val editandoId: String? = null,
    val mostrarFormulario: Boolean = false
)

class EmpresasViewModel : ViewModel() {

    private val repository = EmpresaRepository()
    private val _uiState = MutableStateFlow(EmpresasUiState())
    val uiState: StateFlow<EmpresasUiState> = _uiState.asStateFlow()

    init {
        carregarEmpresas()
    }

    fun carregarEmpresas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            repository.listarEmpresas().fold(
                onSuccess = { lista -> _uiState.update { it.copy(isLoading = false, empresas = lista) } },
                onFailure = { _uiState.update { it.copy(isLoading = false, erro = "Erro ao carregar empresas") } }
            )
        }
    }

    fun onCnpjChange(v: String) { _uiState.update { it.copy(cnpj = v) } }
    fun onNomeChange(v: String) { _uiState.update { it.copy(nome = v) } }
    fun onEnderecoChange(v: String) { _uiState.update { it.copy(endereco = v) } }
    fun onGestorChange(v: String) { _uiState.update { it.copy(gestor = v) } }
    fun onInfoChange(v: String) { _uiState.update { it.copy(info = v) } }

    fun abrirFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = true,
                editandoId = null,
                cnpj = "",
                nome = "",
                endereco = "",
                gestor = "",
                info = ""
            )
        }
    }

    fun fecharFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = false,
                editandoId = null,
                cnpj = "",
                nome = "",
                endereco = "",
                gestor = "",
                info = ""
            )
        }
    }

    fun iniciarEdicao(empresa: Empresa) {
        _uiState.update {
            it.copy(
                mostrarFormulario = true,
                editandoId = empresa.id,
                cnpj = empresa.cnpj,
                nome = empresa.nome,
                endereco = empresa.endereco,
                gestor = empresa.gestor_manutencao,
                info = empresa.informacoes_adicionais ?: ""
            )
        }
    }

    fun salvar() {
        val state = _uiState.value
        val empresa = Empresa(
            cnpj = state.cnpj,
            nome = state.nome,
            endereco = state.endereco,
            gestor_manutencao = state.gestor,
            informacoes_adicionais = state.info.ifBlank { null }
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val result = if (state.editandoId != null) {
                repository.atualizarEmpresa(state.editandoId, empresa)
            } else {
                repository.criarEmpresa(empresa)
            }
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sucesso = "Empresa salva",
                            mostrarFormulario = false,
                            editandoId = null,
                            cnpj = "",
                            nome = "",
                            endereco = "",
                            gestor = "",
                            info = ""
                        )
                    }
                    carregarEmpresas()
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, erro = "Erro ao salvar empresa") }
                }
            )
        }
    }

    fun deletar(id: String) {
        viewModelScope.launch {
            repository.deletarEmpresa(id).fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            empresas = state.empresas.filter { it.id != id },
                            sucesso = "Empresa removida"
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(erro = "Erro ao remover empresa") }
                }
            )
        }
    }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, sucesso = null) }
    }
}
