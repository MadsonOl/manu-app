package com.manu.manu_app.data.repository

import com.manu.manu_app.data.model.Funcao
import com.manu.manu_app.data.model.Profissional
import com.manu.manu_app.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfissionalRepository {

    private val api = RetrofitClient.apiService

    suspend fun listarProfissionais(): Result<List<Profissional>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.listarProfissionais())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun criarProfissional(p: Profissional): Result<Profissional> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.criarProfissional(p))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun atualizarProfissional(id: String, p: Profissional): Result<Profissional> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.atualizarProfissional(id, p))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deletarProfissional(id: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.deletarProfissional(id)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun listarFuncoes(): Result<List<Funcao>> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.listarFuncoes())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun criarFuncao(funcao: Funcao): Result<Funcao> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(api.criarFuncao(funcao))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
