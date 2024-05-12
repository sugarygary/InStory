package com.sugarygary.instory.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.instory.data.remote.response.Story
import com.sugarygary.instory.data.repository.PostRepository
import com.sugarygary.instory.data.repository.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _storyDetail = MutableLiveData<State<Story>>()
    val storyDetail: LiveData<State<Story>>
        get() = _storyDetail

    fun fetchStoryDetail(id: String) {
        viewModelScope.launch {
            postRepository.fetchStoryDetail(id).asFlow().collect {
                _storyDetail.postValue(it)
            }
        }
    }
}