package com.sugarygary.instory.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sugarygary.instory.data.local.database.InstoryDatabase
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.data.remote.retrofit.StoryApiService
import com.sugarygary.instory.util.handleError
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val storyApiService: StoryApiService,
    private val instoryDatabase: InstoryDatabase,
    @ApplicationContext private val context: Context
) : PostRepository {
    override suspend fun fetchStories(): LiveData<State<List<Story>>> = liveData {
        emit(State.Loading)
        try {
            val response = storyApiService.fetchStories()
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                if (response.listStory.isEmpty()) {
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

    override fun fetchStoriesWithPaging(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            remoteMediator = StoryRemoteMediator(context, instoryDatabase, storyApiService),
            pagingSourceFactory = {
                instoryDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    override suspend fun fetchStoriesWithLocation(): LiveData<State<List<Story>>> = liveData {
        emit(State.Loading)
        try {
            val response = storyApiService.fetchStoriesWithLocation()
            if (response.error) {
                emit(State.Error(response.message))
            } else {
                if (response.listStory.isEmpty()) {
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

    override suspend fun postStory(
        file: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): LiveData<State<String>> =
        liveData {
            emit(State.Loading)
            val descriptionBody = description.toRequestBody("multipart/form-data".toMediaType())
            var latitudeBody: RequestBody? = null
            if (latitude != null) {
                latitudeBody =
                    latitude.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            var longitudeBody: RequestBody? = null
            if (longitude != null) {
                longitudeBody =
                    latitude.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            try {
                val response = storyApiService.postStory(
                    multipartBody,
                    descriptionBody,
                    latitudeBody,
                    longitudeBody
                )
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