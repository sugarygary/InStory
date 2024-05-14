package com.sugarygary.instory.di

import android.content.Context
import androidx.room.Room
import com.sugarygary.instory.data.local.database.InstoryDatabase
import com.sugarygary.instory.data.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InstoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            InstoryDatabase::class.java,
            "instory_database"
        ).build()
    }
}