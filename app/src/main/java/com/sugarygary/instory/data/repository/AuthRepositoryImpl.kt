package com.sugarygary.instory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sugarygary.instory.data.local.datastore.DataStoreManager
import com.sugarygary.instory.data.remote.retrofit.StoryApiService
import com.sugarygary.instory.util.handleError
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val storyApiService: StoryApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {
    override suspend fun checkToken(): LiveData<State<String>> = liveData {
        emit(State.Loading)
        try {
            if (dataStoreManager.storyJwtToken.first().isNullOrEmpty()) {
                emit(State.Error("Unauthorized"))
                return@liveData
            }
            val response = storyApiService.fetchStories()
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                emit(State.Success(response.message))
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

    override suspend fun login(email: String, password: String): LiveData<State<String>> =
        liveData {
            emit(State.Loading)
            try {
                val response = storyApiService.login(email, password)
                if (response.error) {
                    emit(State.Error(response.message))
                } else {
                    if (response.loginResult != null) {
                        dataStoreManager.setApiToken(response.loginResult.token)
                    }
                    emit(State.Success(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> emit(State.Error(e.handleError()))
                    else -> emit(State.Error("Unexpected error"))
                }
            }
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<State<String>> = liveData {
        emit(State.Loading)
        try {
            val response = storyApiService.register(name, email, password)
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                emit(State.Success(response.message))
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

    override suspend fun logout() {
        dataStoreManager.removeToken()
    }
}