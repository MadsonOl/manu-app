package com.manu.manu_app.data.remote

import com.manu.manu_app.data.model.ChamadoRequest
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.model.Empresa
import com.manu.manu_app.data.model.Funcao
import com.manu.manu_app.data.model.OrdemServicoRequest
import com.manu.manu_app.data.model.OrdemServicoResponse
import com.manu.manu_app.data.model.Profissional
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("empresas")
    suspend fun criarEmpresa(@Body empresa: Empresa): Empresa

    @GET("empresas")
    suspend fun listarEmpresas(): List<Empresa>

    @PUT("empresas/{id}")
    suspend fun atualizarEmpresa(@Path("id") id: String, @Body empresa: Empresa): Empresa

    @DELETE("empresas/{id}")
    suspend fun deletarEmpresa(@Path("id") id: String)

    @POST("profissionais")
    suspend fun criarProfissional(@Body profissional: Profissional): Profissional

    @GET("profissionais")
    suspend fun listarProfissionais(): List<Profissional>

    @PUT("profissionais/{id}")
    suspend fun atualizarProfissional(@Path("id") id: String, @Body profissional: Profissional): Profissional

    @DELETE("profissionais/{id}")
    suspend fun deletarProfissional(@Path("id") id: String)

    @POST("funcoes")
    suspend fun criarFuncao(@Body funcao: Funcao): Funcao

    @GET("funcoes")
    suspend fun listarFuncoes(): List<Funcao>
}
