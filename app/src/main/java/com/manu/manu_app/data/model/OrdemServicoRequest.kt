package com.manu.manu_app.data.model

data class OrdemServicoRequest(
    val local: String,
    val descricao: String,
    val prioridade: String,
    val solicitante: String,
    val profissional: String? = null,
    val chamado_id: String? = null
)
