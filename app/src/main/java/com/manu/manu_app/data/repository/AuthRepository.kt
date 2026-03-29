package com.manu.manu_app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.manu.manu_app.data.remote.RetrofitClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String, senha: String): Result<Unit> {
        return try {
            suspendCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        if (user != null) {
                            user.getIdToken(false).addOnSuccessListener { tokenResult ->
                                RetrofitClient.authToken = tokenResult.token
                                continuation.resume(Result.success(Unit))
                            }.addOnFailureListener { exception ->
                                continuation.resume(Result.failure(exception))
                            }
                        } else {
                            continuation.resume(Result.failure(Exception("Usuário não encontrado")))
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.failure(exception))
                    }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        RetrofitClient.authToken = null
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    suspend fun refreshToken() {
        val user = firebaseAuth.currentUser ?: return
        suspendCoroutine { continuation ->
            user.getIdToken(false).addOnSuccessListener { tokenResult ->
                RetrofitClient.authToken = tokenResult.token
                continuation.resume(Unit)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}
