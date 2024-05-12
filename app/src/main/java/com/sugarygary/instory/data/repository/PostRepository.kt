package com.sugarygary.instory.data.repository

import androidx.lifecycle.LiveData
import com.sugarygary.instory.data.remote.response.Story
import java.io.File

interface PostRepository {
    suspend fun fetchStories(): LiveData<State<List<Story>>>
    suspend fun fetchStoryDetail(id: String): LiveData<State<Story>>
    suspend fun postStory(file: File, description: String): LiveData<State<String>>
}