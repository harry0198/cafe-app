package me.harrydrummond.cafeapplication.ui.common.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

class CompleteProfileViewModel : ViewModel() {
    private var userId: String? = null
    private var userModel: UserModel? = null
    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val _uiState: MutableLiveData<CreateProfileUiState> = MutableLiveData(
        CreateProfileUiState()
    )
    val uiState: LiveData<CreateProfileUiState> get() = _uiState

    // On initialize, begin loading the user
    init {
        userRepository.getLoggedInUserId().let {
            _uiState.value = _uiState.value?.copy(loading = true)
            userId = it
            if (it != null) {
                userRepository.getUser(it).addOnSuccessListener { user ->
                    userModel = user
                    val splitName = userModel?.fullName?.split(" ")
                    val firstName = splitName?.get(0)
                    val lastName = splitName?.get(1)
                    val phoneNumber = userModel?.phoneNumber
                    _uiState.value = _uiState.value?.copy(loading = false, firstName = firstName?: "", lastName = lastName?: "", phoneNumber = phoneNumber?: "")
                }.addOnFailureListener {
                    _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Failed to load user")
                }
            }
        }
    }


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

        val fullName = "$firstName $lastName"

        // Prevent mutable
        val userModel = userModel
        val userId = userId

        if (userModel == null || userId == null) {
            _uiState.value = uiState.value?.copy(loading = false, errorMessage = "Your account has not loaded yet")
            return
        }
        val user = userModel.copy(phoneNumber = phoneNumber, fullName = fullName)

        userRepository.saveUser(userId, user).addOnCompleteListener { task ->
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

    /**
     * Indicates that the error message has been shown and can be removed from the ui state.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Interface that activities must implement to allow for button handling
     */
    interface ValidationListener {
        fun onValidationSuccess()
    }

    /**
     * Data class for the CreateProfile UI State.
     * Includes fields for loading, errors and events.
     */
    data class CreateProfileUiState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val event: Event? = null,
        val firstName: String = "",
        val lastName: String = "",
        val phoneNumber: String = ""
    )

    /**
     * A type for sending events to the View Layer.
     */
    sealed interface Event {
        data object ProfileSaved: Event
    }
}