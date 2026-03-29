package com.manu.manu_app.data.model

data class OrdemServicoResponse(
    val id: String,
    val local: String,
    val descricao: String,
    val prioridade: String,
    val solicitante: String,
    val profissional: String?,
    val status: String,
    val chamado_id: String?,
    val data: String
)
