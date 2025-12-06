package com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdBuddyDatabase
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdRepository
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentUser: UserEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BirdRepository

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        val db = BirdBuddyDatabase.getInstance(application)
        repository = BirdRepository(
            userDao = db.userDao(),
            birdDao = db.birdDao(),
            userBirdDao = db.userBirdDao()
        )
    }

    fun login(username: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.login(username.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { user ->
                    AuthUiState(currentUser = user, isLoading = false, errorMessage = null)
                },
                onFailure = { ex ->
                    _uiState.value.copy(isLoading = false, errorMessage = ex.message)
                }
            )
        }
    }

    fun register(username: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.registerUser(username.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { user ->
                    AuthUiState(currentUser = user, isLoading = false, errorMessage = null)
                },
                onFailure = { ex ->
                    _uiState.value.copy(isLoading = false, errorMessage = ex.message)
                }
            )
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun getRepository(): BirdRepository = repository
}
