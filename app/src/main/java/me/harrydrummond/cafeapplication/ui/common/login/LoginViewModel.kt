package me.harrydrummond.cafeapplication.ui.common.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject


/**
 * LoginViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to login.
 *
 * @see LoginActivity
 * @author Harry Drummond
 */
class LoginViewModel: ViewModel() {

    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val _uiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = _uiState.value?.copy(loading = true)
        val loginTask = userRepository.loginUser(email, password)
        loginTask.addOnSuccessListener {
            getUser(userRepository.getLoggedInUserId()!!).addOnCompleteListener { userTask ->
                    if (userTask.isSuccessful) {
                        val event = when (userTask.result?.role) {
                            Role.EMPLOYEE -> Event.GoToAdminApp
                            else -> Event.GoToCustomerApp
                        }
                        _uiState.value = _uiState.value?.copy(loading = false, event = event)
                    } else {
                        _uiState.value = _uiState.value?.copy(
                            loading = false,
                            errorMessage = userTask.exception?.message
                        )
                    }
                }
        }
        loginTask.addOnFailureListener { task ->
            when (task) {
                is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException -> {
                    _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Invalid login credentials")
                }
                else -> {
                    _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Unknown error occurred")
                }
            }
        }

    }

    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    private fun getUser(uid: String): Task<UserModel?> {
        return userRepository.getUser(uid)
    }
}

/**
 * Data class for passing UI State to the View
 */
data class LoginUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null
)

/**
 * Event objects for passing Events to the View
 */
sealed interface Event {
    data object GoToAdminApp: Event
    data object GoToCustomerApp: Event
}