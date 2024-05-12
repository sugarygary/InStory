package com.sugarygary.instory.data.remote.retrofit

import com.sugarygary.instory.data.remote.response.AuthResponse
import com.sugarygary.instory.data.remote.response.ErrorResponse
import com.sugarygary.instory.data.remote.response.StoryDetailResponse
import com.sugarygary.instory.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @GET("stories")
    suspend fun fetchStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun fetchStoryDetail(
        @Path("id") id: String
    ): StoryDetailResponse

    @POST("stories")
    @Multipart
    suspend fun postStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): ErrorResponse

}