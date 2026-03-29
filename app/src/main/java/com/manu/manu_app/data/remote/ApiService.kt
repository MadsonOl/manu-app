package com.manu.manu_app.data.remote

import com.manu.manu_app.data.model.ChamadoRequest
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.model.OrdemServicoRequest
import com.manu.manu_app.data.model.OrdemServicoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("chamados")
    suspend fun criarChamado(@Body request: ChamadoRequest): ChamadoResponse

    @GET("chamados")
    suspend fun listarChamados(): List<ChamadoResponse>

    @GET("chamados/{chamadoId}")
    suspend fun buscarChamado(@Path("chamadoId") chamadoId: String): ChamadoResponse

    @POST("ordens-servico")
    suspend fun criarOrdemServico(@Body request: OrdemServicoRequest): OrdemServicoResponse

    @GET("ordens-servico")
    suspend fun listarOrdensServico(): List<OrdemServicoResponse>
}
