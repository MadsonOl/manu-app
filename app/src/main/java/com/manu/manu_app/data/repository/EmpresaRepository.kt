package com.manu.manu_app.data.repository

import com.manu.manu_app.data.model.Empresa
import com.manu.manu_app.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmpresaRepository {

    private val api = RetrofitClient.apiService

    suspend fun listarEmpresas(): Result<List<Empresa>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.listarEmpresas())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun criarEmpresa(empresa: Empresa): Result<Empresa> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.criarEmpresa(empresa))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun atualizarEmpresa(id: String, empresa: Empresa): Result<Empresa> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.atualizarEmpresa(id, empresa))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deletarEmpresa(id: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.deletarEmpresa(id)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
