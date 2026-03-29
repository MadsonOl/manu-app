package com.manu.manu_app.data.repository

import com.manu.manu_app.data.model.OrdemServicoRequest
import com.manu.manu_app.data.model.OrdemServicoResponse
import com.manu.manu_app.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrdemRepository {

    private val api = RetrofitClient.apiService

    suspend fun criarOrdem(request: OrdemServicoRequest): Result<OrdemServicoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.criarOrdemServico(request))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun listarOrdens(): Result<List<OrdemServicoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.listarOrdensServico())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
