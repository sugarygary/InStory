package com.sugarygary.instory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.data.remote.retrofit.StoryApiService
import com.sugarygary.instory.util.handleError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val storyApiService: StoryApiService
) : PostRepository {
    override suspend fun fetchStories(): LiveData<State<List<Story>>> = liveData {
        emit(State.Loading)
        try {
            val response = storyApiService.fetchStories()
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                if (response.listStory.isNullOrEmpty()) {
                    emit(State.Empty)
                } else {
                    emit(State.Success(response.listStory))
                }
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

    override suspend fun fetchStoryDetail(id: String): LiveData<State<Story>> = liveData {
        emit(State.Loading)
        try {
            val response = storyApiService.fetchStoryDetail(id)
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                if (response.story == null) {
                    emit(State.Empty)
                } else {
                    emit(State.Success(response.story))
                }
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> emit(State.Error(e.handleError()))
                else -> emit(State.Error("Unexpected error"))
            }
        }
    }

    override suspend fun postStory(file: File, description: String): LiveData<State<String>> =
        liveData {
            emit(State.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            try {
                val response = storyApiService.postStory(multipartBody, requestBody)
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
}