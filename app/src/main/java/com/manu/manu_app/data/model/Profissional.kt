package com.manu.manu_app.data.model

data class Profissional(
    val id: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val rg: String = "",
    val cpf: String = "",
    val funcao: String? = null,
    val funcao_id: String? = null
)
