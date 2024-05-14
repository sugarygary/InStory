package com.sugarygary.instory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sugarygary.instory.data.local.database.dao.RemoteKeyDao
import com.sugarygary.instory.data.local.database.dao.StoryDao
import com.sugarygary.instory.data.local.database.model.RemoteKey
import com.sugarygary.instory.data.remote.response.Story

@Database(
    entities = [Story::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class InstoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}