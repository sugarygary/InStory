package com.sugarygary.instory.di

import com.sugarygary.instory.data.repository.AuthRepository
import com.sugarygary.instory.data.repository.AuthRepositoryImpl
import com.sugarygary.instory.data.repository.PostRepository
import com.sugarygary.instory.data.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository
}