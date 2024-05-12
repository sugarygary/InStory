package com.sugarygary.instory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.data.repository.AuthRepository
import com.sugarygary.instory.data.repository.PostRepository
import com.sugarygary.instory.data.repository.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    private val _stories = MutableLiveData<State<List<Story>>>()
    val stories: LiveData<State<List<Story>>>
        get() = _stories

    suspend fun logout() {
        authRepository.logout()
    }

    fun fetchStories() {
        viewModelScope.launch {
            postRepository.fetchStories().asFlow().collect {
                _stories.postValue(it)
            }
        }
    }
}