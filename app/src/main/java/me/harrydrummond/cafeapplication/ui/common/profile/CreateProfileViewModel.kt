package me.harrydrummond.cafeapplication.ui.common.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

/**
 * CreateProfileViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to save the user profile to the database.
 *
 * @see CreateProfileActivity
 * @author Harry Drummond
 */
class CreateProfileViewModel: ViewModel() {

    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val _uiState: MutableLiveData<CreateProfileUiState> = MutableLiveData(
        CreateProfileUiState()
    )
    val uiState: LiveData<CreateProfileUiState> get() = _uiState

    /**
     * Saves the user profile information to the database. If it is unable to save, the ui State
     * is updated with the error messages.
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param phoneNumber The phone number of the user
     */
    fun saveProfileInformation(firstName: String, lastName: String, phoneNumber: String) {
        _uiState.value = _uiState.value?.copy(loading = true)

        val model = UserModel(phoneNumber, "$firstName $lastName")
        userRepository.getLoggedInUserId().let {
            if (it != null) {
                userRepository.saveUser(it, model).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value =
                            _uiState.value?.copy(loading = false, event = Event.ProfileSaved)
                    } else {
                        _uiState.value = _uiState.value?.copy(
                            loading = false,
                            errorMessage = "Unable to save profile"
                        )
                    }
                }
            }
        }
    }

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