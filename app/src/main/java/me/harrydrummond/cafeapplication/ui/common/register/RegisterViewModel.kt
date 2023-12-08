package me.harrydrummond.cafeapplication.ui.common.register

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
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.logic.validators.ValidatedPasswordResult
import javax.inject.Inject


/**
 * RegisterViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to register a new user.
 *
 * @see RegisterActivity
 * @author Harry Drummond
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val customerRepository: IUserRepository<Customer>, private val employeeRepository: IUserRepository<Employee>): ViewModel() {

    private val _uiState: MutableLiveData<RegisterUIState> = MutableLiveData(RegisterUIState())
    val uiState: LiveData<RegisterUIState> get() = _uiState

    /**
     * Registers the user using a email and password authentication method.
     * If any errors occur, an appropriate error message is posted to the UiState.
     *
     * @param username username to sign up with
     * @param password Password to sign up account with
     * @param accountType Type of account to create
     * @see RegisterUIState
     */
    fun register(username: String, password: String, accountType: Role) {
        // Do validations
        val validateUsername = Validators.validateUsername(username)
        val validatePassword = Validators.validatePassword(password)

        _uiState.value = _uiState.value?.copy(
            loading = false,
            validatedUsername = validateUsername,
            validatedPassword = validatePassword
        )

        if (!validateUsername.isValid || !validatePassword.allDoPass()) {
            return
        }

        _uiState.value = _uiState.value?.copy(loading = true)

        // In background, register user.
        viewModelScope.launch {
            when (accountType) {
                Role.CUSTOMER -> registerCustomer(username, password)
                Role.EMPLOYEE -> registerEmployee(username, password)
            }
        }
    }

    private fun registerEmployee(username: String, password: String) {
        val employee = Employee(
            -1,
            null,
            null,
            null,
            username,
            password,
            true
        )
        val save = employeeRepository.save(employee)
        saveCodeHandler(save, Role.EMPLOYEE)
    }

    private fun registerCustomer(username: String, password: String) {
        val customer = Customer(
            -1,
            null,
            null,
            null,
            username,
            password,
            true
        )
        val save = customerRepository.save(customer)
        saveCodeHandler(save, Role.CUSTOMER)
    }

    private fun saveCodeHandler(save: Int, role: Role) {
        // Handles the codes save outputs
        when (save) {
            -3 -> _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Username is taken"))
            -1 -> _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Unable to register"))
            else -> {
                AuthenticatedUser.getInstance().login(save, role)
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.UserWasRegistered))
            }
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
    val event: Event? = null,
    val validatedUsername: ValidatedResult = ValidatedResult(true, null),
    val validatedPassword: ValidatedPasswordResult = ValidatedPasswordResult(true, true, true, true, true)
)