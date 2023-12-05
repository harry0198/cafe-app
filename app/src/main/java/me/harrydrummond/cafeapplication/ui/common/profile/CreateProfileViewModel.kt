package me.harrydrummond.cafeapplication.ui.common.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * CreateProfileViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to save the user profile to the database.
 *
 * @see CreateProfileActivity
 * @author Harry Drummond
 */
class CreateProfileViewModel: ViewModel() {

    private val _uiState: MutableLiveData<CreateProfileUiState> = MutableLiveData(
        CreateProfileUiState()
    )
    val uiState: LiveData<CreateProfileUiState> get() = _uiState

    /**
     * Indicates that the error message has been shown and can be removed from the ui state.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * Data class for the CreateProfile UI State.
 * Includes fields for loading, errors and events.
 */
data class CreateProfileUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null,
)

/**
 * A type for sending events to the View Layer.
 */
sealed interface Event {
    data object ProfileSaved: Event
}