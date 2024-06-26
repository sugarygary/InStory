package com.sugarygary.instory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sugarygary.instory.data.remote.response.Story
import java.io.File

interface PostRepository {
    suspend fun fetchStories(): LiveData<State<List<Story>>>
    fun fetchStoriesWithPaging(): LiveData<PagingData<Story>>
    suspend fun fetchStoriesWithLocation(): LiveData<State<List<Story>>>
    suspend fun fetchStoryDetail(id: String): LiveData<State<Story>>
    suspend fun postStory(
        file: File,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): LiveData<State<String>>
}