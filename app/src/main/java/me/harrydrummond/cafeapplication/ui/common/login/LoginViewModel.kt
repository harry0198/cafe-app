package me.harrydrummond.cafeapplication.ui.common.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.logic.validators.ValidatedResult
import me.harrydrummond.cafeapplication.logic.validators.Validators
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.User
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject


/**
 * LoginViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to login.
 *
 * @see LoginActivity
 * @author Harry Drummond
 */

@HiltViewModel
class LoginViewModel @Inject constructor(private val customerRepository: IUserRepository<Customer>, private val employeeRepository: IUserRepository<Employee>): ViewModel() {

    private val _uiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String, role: Role) {
        _uiState.postValue(_uiState.value?.copy(loading = true))

        val usernameValidation = Validators.validateUsername(username)
        val passwordValidation = Validators.validateNotEmpty(password)
        _uiState.postValue(_uiState.value?.copy(loading = false, usernameValidation = usernameValidation, passwordValidation = passwordValidation))

        // Validate inputs
        if (!usernameValidation.isValid || !passwordValidation.isValid) {
            return
        }

        // Run async logging in the user
        viewModelScope.launch {
            val user: User?
            if (role == Role.EMPLOYEE) {
                val employeeId = employeeRepository.getEntityIdByUsernameAndPassword(username, password)
                user = employeeRepository.getById(employeeId)
            } else {
                val customerId = customerRepository.getEntityIdByUsernameAndPassword(username, password)
                user = customerRepository.getById(customerId)
            }

            // If user does not exist or isn't active..
            if (user == null || !user.isActive) {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Invalid credentials"))
                return@launch
            }
            AuthenticatedUser.getInstance().login(user.id, role)
            _uiState.postValue(_uiState.value?.copy(loading = false, event = if (role == Role.EMPLOYEE) Event.GoToAdminApp else Event.GoToCustomerApp))
        }
    }

    fun errorMessageShown() {
        _uiState.postValue(_uiState.value?.copy(errorMessage = null))
    }
}

/**
 * Data class for passing UI State to the View
 */
data class LoginUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null,
    val usernameValidation: ValidatedResult = ValidatedResult(true, null),
    val passwordValidation: ValidatedResult = ValidatedResult(true, null)
)

/**
 * Event objects for passing Events to the View
 */
sealed interface Event {
    data object GoToAdminApp: Event
    data object GoToCustomerApp: Event
}