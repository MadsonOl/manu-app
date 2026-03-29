package com.manu.manu_app.ui.navigation

object Routes {
    const val HOME = "home"
    const val SOLICITANTE = "solicitante"
    const val LOGIN = "login"
    const val CHAMADOS = "chamados"
    const val CHAMADO_DETALHE = "chamado_detalhe/{chamadoId}"
    const val ORDENS = "ordens"
    const val GERAR_OS = "gerar_os/{chamadoId}"

    fun chamadoDetalhe(id: String) = "chamado_detalhe/$id"
    fun gerarOs(id: String) = "gerar_os/$id"
}
