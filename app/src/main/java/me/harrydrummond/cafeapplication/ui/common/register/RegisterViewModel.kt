package me.harrydrummond.cafeapplication.ui.common.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository


/**
 * RegisterViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to register a new user.
 *
 * @see RegisterActivity
 * @author Harry Drummond
 */
class RegisterViewModel: ViewModel() {

    private val productRepository: IProductRepository = FirestoreProductRepository()
    private val userRepository: IUserRepository = FirestoreUserRepository(productRepository)
    private val _uiState: MutableLiveData<RegisterUIState> = MutableLiveData(RegisterUIState())
    val uiState: LiveData<RegisterUIState> get() = _uiState

    /**
     * Registers the user using a email and password authentication method.
     * If any errors occur, an appropriate error message is posted to the UiState.
     *
     * @see RegisterUIState
     */
    fun register(email: String, password: String) {
        _uiState.value = _uiState.value?.copy(loading = true)

        userRepository.registerUser(email, password).addOnSuccessListener { task ->
            userRepository.saveUser(userRepository.getLoggedInUserId()!!, UserModel()).addOnSuccessListener {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.UserWasRegistered)
            }.addOnFailureListener { _ ->
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Fatal Error: Please contact an administrator.")
            }
        }.addOnFailureListener { task ->
            val error = when (task) {
                is FirebaseAuthUserCollisionException -> "A user with this email already exists"
                is FirebaseAuthWeakPasswordException -> "Your password is too weak"
                else -> "An unknown error occurred"
            }

            _uiState.value = _uiState.value?.copy(errorMessage = error, loading = false)
        }
    }

    /**
     * Marks that an error message has been shown in the ui and can now be removed from the UIState.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * Event definition for the product view model.
 */
sealed interface Event {
    data object UserWasRegistered: Event
}

/**
 * UI State data class to hold properties for the UI View
 */
data class RegisterUIState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null
)