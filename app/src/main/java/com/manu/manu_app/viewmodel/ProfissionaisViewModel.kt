package com.manu.manu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manu.manu_app.data.model.Funcao
import com.manu.manu_app.data.model.Profissional
import com.manu.manu_app.data.repository.ProfissionalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfissionaisUiState(
    val profissionais: List<Profissional> = emptyList(),
    val funcoes: List<Funcao> = emptyList(),
    val isLoading: Boolean = false,
    val erro: String? = null,
    val sucesso: String? = null,
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val rg: String = "",
    val cpf: String = "",
    val funcaoSelecionada: String = "",
    val editandoId: String? = null,
    val mostrarFormulario: Boolean = false,
    val nomeFuncao: String = "",
    val mostrarFormularioFuncao: Boolean = false
)

class ProfissionaisViewModel : ViewModel() {

    private val repository = ProfissionalRepository()
    private val _uiState = MutableStateFlow(ProfissionaisUiState())
    val uiState: StateFlow<ProfissionaisUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    fun carregarDados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            launch {
                repository.listarProfissionais().fold(
                    onSuccess = { lista -> _uiState.update { it.copy(profissionais = lista) } },
                    onFailure = { _uiState.update { it.copy(erro = "Erro ao carregar profissionais") } }
                )
            }
            launch {
                repository.listarFuncoes().fold(
                    onSuccess = { lista -> _uiState.update { it.copy(funcoes = lista) } },
                    onFailure = { }
                )
            }
        }.invokeOnCompletion {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onNomeChange(v: String) { _uiState.update { it.copy(nome = v) } }
    fun onTelefoneChange(v: String) { _uiState.update { it.copy(telefone = v) } }
    fun onEmailChange(v: String) { _uiState.update { it.copy(email = v) } }
    fun onRgChange(v: String) { _uiState.update { it.copy(rg = v) } }
    fun onCpfChange(v: String) { _uiState.update { it.copy(cpf = v) } }
    fun onFuncaoChange(v: String) { _uiState.update { it.copy(funcaoSelecionada = v) } }

    fun abrirFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = true,
                editandoId = null,
                nome = "",
                telefone = "",
                email = "",
                rg = "",
                cpf = "",
                funcaoSelecionada = ""
            )
        }
    }

    fun fecharFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = false,
                editandoId = null,
                nome = "",
                telefone = "",
                email = "",
                rg = "",
                cpf = "",
                funcaoSelecionada = ""
            )
        }
    }

    fun iniciarEdicao(profissional: Profissional) {
        _uiState.update {
            it.copy(
                mostrarFormulario = true,
                editandoId = profissional.id,
                nome = profissional.nome,
                telefone = profissional.telefone,
                email = profissional.email,
                rg = profissional.rg,
                cpf = profissional.cpf,
                funcaoSelecionada = profissional.funcao_id ?: ""
            )
        }
    }

    fun salvar() {
        val state = _uiState.value
        val profissional = Profissional(
            nome = state.nome,
            telefone = state.telefone,
            email = state.email,
            rg = state.rg,
            cpf = state.cpf,
            funcao_id = state.funcaoSelecionada.ifBlank { null }
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val result = if (state.editandoId != null) {
                repository.atualizarProfissional(state.editandoId, profissional)
            } else {
                repository.criarProfissional(profissional)
            }
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sucesso = "Profissional salvo",
                            mostrarFormulario = false,
                            editandoId = null,
                            nome = "",
                            telefone = "",
                            email = "",
                            rg = "",
                            cpf = "",
                            funcaoSelecionada = ""
                        )
                    }
                    carregarDados()
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, erro = "Erro ao salvar profissional") }
                }
            )
        }
    }

    fun deletar(id: String) {
        viewModelScope.launch {
            repository.deletarProfissional(id).fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            profissionais = state.profissionais.filter { it.id != id },
                            sucesso = "Profissional removido"
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(erro = "Erro ao remover profissional") }
                }
            )
        }
    }

    fun onNomeFuncaoChange(v: String) { _uiState.update { it.copy(nomeFuncao = v) } }

    fun abrirFormularioFuncao() {
        _uiState.update { it.copy(mostrarFormularioFuncao = true, nomeFuncao = "") }
    }

    fun fecharFormularioFuncao() {
        _uiState.update { it.copy(mostrarFormularioFuncao = false, nomeFuncao = "") }
    }

    fun salvarFuncao() {
        val nome = _uiState.value.nomeFuncao
        if (nome.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            repository.criarFuncao(Funcao(nome = nome)).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sucesso = "Funcao criada",
                            mostrarFormularioFuncao = false,
                            nomeFuncao = ""
                        )
                    }
                    // Recarrega funcoes
                    repository.listarFuncoes().onSuccess { lista ->
                        _uiState.update { it.copy(funcoes = lista) }
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, erro = "Erro ao criar funcao") }
                }
            )
        }
    }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, sucesso = null) }
    }
}
