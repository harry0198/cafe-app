package me.harrydrummond.cafeapplication.ui.common.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.ValidatedResult
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject


/**
 * RegisterViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to register a new user.
 *
 * @see RegisterActivity
 * @author Harry Drummond
 */
class RegisterViewModel @Inject constructor(private val customerRepository: IUserRepository<Customer>): ViewModel() {

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
        val validateUsername = Validators.validateEmail(username)
        val validatePassword = Validators.validatePassword(password)

        _uiState.value = _uiState.value?.copy(
            loading = false,
            validatedUsername = validateUsername,
            validatedPassword = validatePassword
        )

        if (!validateUsername.isValid || !validatePassword.isValid) {
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

        when (save) {
            -3 -> _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Username taken!"))
            -1 -> _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Unable to register"))
            else -> _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.UserWasRegistered))
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
    val validatedPassword: ValidatedResult = ValidatedResult(true, null)
)