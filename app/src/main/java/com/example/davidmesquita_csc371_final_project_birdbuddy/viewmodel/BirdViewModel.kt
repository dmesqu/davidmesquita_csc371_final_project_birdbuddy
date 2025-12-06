package com.example.davidmesquita_csc371_final_project_birdbuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdBuddyDatabase
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdEntity
import com.example.davidmesquita_csc371_final_project_birdbuddy.data.BirdRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class IdentifyAnswers(
    val size: String? = null,
    val color: String? = null,
    val habitat: String? = null
)

data class UserBirdDisplay(
    val bird: BirdEntity,
    val imageUri: String?
)

data class BirdUiState(
    val allBirds: List<BirdEntity> = emptyList(),
    val myBirds: List<UserBirdDisplay> = emptyList(),
    val identifyAnswers: IdentifyAnswers = IdentifyAnswers(),
    val identifyMatches: List<BirdEntity> = emptyList(),
    val currentBirdImageUri: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class BirdViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BirdRepository

    private val _uiState = MutableStateFlow(BirdUiState())
    val uiState: StateFlow<BirdUiState> = _uiState

    private var currentUserId: Long? = null

    init {
        val db = BirdBuddyDatabase.getInstance(application)
        repository = BirdRepository(
            userDao = db.userDao(),
            birdDao = db.birdDao(),
            userBirdDao = db.userBirdDao()
        )
        viewModelScope.launch {
            repository.ensureBirdsSeeded()
            loadAllBirdsInternal()
        }
    }

    fun setCurrentUser(userId: Long?) {
        currentUserId = userId
        _uiState.value = _uiState.value.copy(currentBirdImageUri = null)
        if (userId != null) {
            loadUserBirds(userId)
        } else {
            _uiState.value = _uiState.value.copy(myBirds = emptyList())
        }
    }

    private suspend fun loadAllBirdsInternal() {
        val birds = repository.getAllBirds()
        _uiState.value = _uiState.value.copy(allBirds = birds)
    }

    private fun loadUserBirds(userId: Long) {
        viewModelScope.launch {
            val birds = repository.getBirdsForUser(userId)
            val birdsWithImages = birds.map { bird ->
                val uri = repository.getUserBirdImage(userId, bird.id)
                UserBirdDisplay(bird = bird, imageUri = uri)
            }
            _uiState.value = _uiState.value.copy(myBirds = birdsWithImages)
        }
    }

    fun updateSize(size: String) {
        _uiState.value = _uiState.value.copy(
            identifyAnswers = _uiState.value.identifyAnswers.copy(size = size)
        )
        runIdentify()
    }

    fun updateColor(color: String) {
        _uiState.value = _uiState.value.copy(
            identifyAnswers = _uiState.value.identifyAnswers.copy(color = color)
        )
        runIdentify()
    }

    fun updateHabitat(habitat: String) {
        _uiState.value = _uiState.value.copy(
            identifyAnswers = _uiState.value.identifyAnswers.copy(habitat = habitat)
        )
        runIdentify()
    }

    private fun runIdentify() {
        val answers = _uiState.value.identifyAnswers
        viewModelScope.launch {
            val matches = repository.filterBirds(
                size = answers.size,
                color = answers.color,
                habitat = answers.habitat
            )
            _uiState.value = _uiState.value.copy(identifyMatches = matches)
        }
    }

    fun resetIdentify() {
        _uiState.value = _uiState.value.copy(
            identifyAnswers = IdentifyAnswers(),
            identifyMatches = emptyList(),
            message = null
        )
    }

    fun addToMyBirds(bird: BirdEntity, imageUri: String? = null) {
        val userId = currentUserId ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        viewModelScope.launch {
            val added = repository.addBirdToUser(userId, bird.id, imageUri)
            if (added) {
                loadUserBirds(userId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Added ${bird.commonName} to your collection!"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "${bird.commonName} is already in your collection."
                )
            }
        }
    }

    fun loadCurrentBirdImage(birdId: Long) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val uri = repository.getUserBirdImage(userId, birdId)
            _uiState.value = _uiState.value.copy(currentBirdImageUri = uri)
        }
    }

    fun updateCurrentBirdImage(birdId: Long, imageUri: String?) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            repository.updateUserBirdImage(userId, birdId, imageUri)
            val uri = repository.getUserBirdImage(userId, birdId)
            _uiState.value = _uiState.value.copy(currentBirdImageUri = uri)
            loadUserBirds(userId)
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
