package com.sugarygary.instory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.data.repository.AuthRepository
import com.sugarygary.instory.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    postRepository: PostRepository
) : ViewModel() {
    val stories: LiveData<PagingData<Story>> =
        postRepository.fetchStoriesWithPaging().cachedIn(viewModelScope)

    suspend fun logout() {
        authRepository.logout()
    }

}