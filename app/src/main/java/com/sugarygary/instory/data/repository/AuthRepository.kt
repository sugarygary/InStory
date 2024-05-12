package com.sugarygary.instory.data.repository

import androidx.lifecycle.LiveData

interface AuthRepository {
    suspend fun checkToken(): LiveData<State<String>>
    suspend fun login(email: String, password: String): LiveData<State<String>>
    suspend fun register(name: String, email: String, password: String): LiveData<State<String>>
    suspend fun logout()
}