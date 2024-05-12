package com.sugarygary.instory.ui.login

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
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<State<String>>()
    val loginResponse: LiveData<State<String>>
        get() = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).asFlow().collect {
                _loginResponse.postValue(it)
            }
        }
    }
}