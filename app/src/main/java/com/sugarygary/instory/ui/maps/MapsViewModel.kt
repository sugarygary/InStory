package com.sugarygary.instory.ui.maps

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
class MapsViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<State<List<Story>>>()
    val storyResponse: LiveData<State<List<Story>>>
        get() = _storyResponse

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            postRepository.fetchStoriesWithLocation().asFlow().collect {
                _storyResponse.postValue(it)
            }
        }
    }
}