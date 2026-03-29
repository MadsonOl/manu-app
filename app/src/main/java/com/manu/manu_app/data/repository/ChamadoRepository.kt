package com.manu.manu_app.data.repository

import com.manu.manu_app.data.model.ChamadoRequest
import com.manu.manu_app.data.model.ChamadoResponse
import com.manu.manu_app.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChamadoRepository {

    private val api = RetrofitClient.apiService

    suspend fun criarChamado(request: ChamadoRequest): Result<ChamadoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.criarChamado(request))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun listarChamados(): Result<List<ChamadoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.listarChamados())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun buscarChamado(id: String): Result<ChamadoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.buscarChamado(id))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
