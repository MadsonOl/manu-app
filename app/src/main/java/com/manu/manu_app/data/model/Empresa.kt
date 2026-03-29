package com.manu.manu_app.data.model

data class Empresa(
    val id: String = "",
    val cnpj: String = "",
    val nome: String = "",
    val endereco: String = "",
    val gestor_manutencao: String = "",
    val informacoes_adicionais: String? = null
)
