package com.example.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.CartRepository
import com.example.core.domain.model.Result
import com.example.core.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _signOutState = MutableStateFlow<SignOutState>(SignOutState.Idle)
    val signOutState: StateFlow<SignOutState> = _signOutState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            _signOutState.value = SignOutState.Loading
            when (val result = signOutUseCase(Unit)) {
                is Result.Success -> {
                    _signOutState.value = SignOutState.Success
                    cartRepository.clearCart()
                }
                is Result.Error -> _signOutState.value = SignOutState.Error(result.exception?.message)
                else -> _signOutState.value = SignOutState.Idle
            }
        }
    }
}

sealed class SignOutState {
    object Idle : SignOutState()
    object Loading : SignOutState()
    object Success : SignOutState()
    data class Error(val message: String?) : SignOutState()
}