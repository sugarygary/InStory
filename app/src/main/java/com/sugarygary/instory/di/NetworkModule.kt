package com.sugarygary.instory.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sugarygary.instory.BuildConfig
import com.sugarygary.instory.data.local.datastore.DataStoreManager
import com.sugarygary.instory.data.remote.retrofit.StoryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(dataStoreManager: DataStoreManager): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val token = runBlocking { dataStoreManager.storyJwtToken.first() }
        if (token.isNullOrEmpty()) {
            return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        }
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders =
                req.newBuilder().addHeader("Authorization", "Bearer $token").build()
            chain.proceed(requestHeaders)
        }
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor).build()
    }

    @Provides
    fun provideStoryApi(client: OkHttpClient): StoryApiService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(StoryApiService::class.java)
    }
}