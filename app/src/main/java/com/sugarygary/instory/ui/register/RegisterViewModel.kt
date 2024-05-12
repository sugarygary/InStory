package com.sugarygary.instory.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.sugarygary.instory.data.repository.AuthRepository
import com.sugarygary.instory.data.repository.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _registerResponse = MutableLiveData<State<String>>()
    val registerResponse: LiveData<State<String>>
        get() = _registerResponse

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(name, email, password).asFlow().collect {
                _registerResponse.postValue(it)
            }
        }
    }
}