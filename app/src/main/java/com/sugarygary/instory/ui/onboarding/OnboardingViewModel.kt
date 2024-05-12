package com.sugarygary.instory.ui.onboarding

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
class OnboardingViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _authStatus = MutableLiveData<State<String>>()
    val authStatus: LiveData<State<String>>
        get() = _authStatus

    fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.checkToken().asFlow().collect {
                _authStatus.postValue(it)
            }
        }
    }

}