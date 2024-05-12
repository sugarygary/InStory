package com.sugarygary.instory.ui.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.instory.data.repository.PostRepository
import com.sugarygary.instory.data.repository.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageUri: LiveData<Uri?>
        get() = _imageUri

    private val _postResponse = MutableLiveData<State<String>>()
    val postResponse: LiveData<State<String>>
        get() = _postResponse

    fun setImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }

    fun postStory(imageFile: File, description: String) {
        viewModelScope.launch {
            postRepository.postStory(imageFile, description).asFlow().collect {
                _postResponse.postValue(it)
            }
        }
    }
}