package me.harrydrummond.cafeapplication.ui.common.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.ValidatedResult
import me.harrydrummond.cafeapplication.Validators
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

/**
 * CompleteProfileViewModel class which provides the business logic to the view class
 * using the MVVM pattern.
 *
 * @see CompleteProfileFragment
 * @author Harry Drummond
 */
@HiltViewModel
class CompleteProfileViewModel @Inject constructor(private val customerRepository: IUserRepository<Customer>, private val employeeRepository: IUserRepository<Employee>) : ViewModel() {

    private val _uiState: MutableLiveData<CreateProfileUiState> = MutableLiveData(
        CreateProfileUiState()
    )
    val uiState: LiveData<CreateProfileUiState> get() = _uiState

    // On initialize, begin loading the user
    init {
        refreshProfile()
    }

    /**
     * Saves the user profile information to the database. If it is unable to save, the ui State
     * is updated with the error messages.
     *
     * @param email Email of user
     * @param fullName The full name of the user
     * @param phoneNumber The phone number of the user
     */
    fun saveProfileInformation(fullName: String, phoneNumber: String, email: String) {
        _uiState.value = _uiState.value?.copy(loading = true)

        viewModelScope.launch {
            // Check validation
            val fullNameValidated = Validators.validateNotEmpty(fullName)
            val phoneNumberValidated = Validators.validatePhoneNumber(phoneNumber)
            val emailValidated = Validators.validateEmail(email)
            _uiState.value = _uiState.value?.copy(
                loading = false,
                emailValidated = emailValidated,
                fullNameValidated = fullNameValidated,
                phoneNumberValidated = phoneNumberValidated
            )

            if (!fullNameValidated.isValid || !phoneNumberValidated.isValid || !emailValidated.isValid) {
                return@launch
            }

            when (AuthenticatedUser.getInstance().getUserRole()) {
                Role.EMPLOYEE -> saveEmployee(fullName, phoneNumber, email)
                else -> saveCustomer(fullName, phoneNumber, email)
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
     * Indicates that the event has been handled
     */
    fun eventHandled() {
        _uiState.value = _uiState.value?.copy(event = null)
    }

    /**
     * Refreshes the currently logged-in user's profile
     * and delegates success and failure listeners
     */
    private fun refreshProfile() {
        // Run in background
        viewModelScope.launch {

            when (AuthenticatedUser.getInstance().getUserRole()) {
                Role.EMPLOYEE -> refreshEmployee()
                else -> refreshCustomer()
            }
        }
    }

    private fun saveCustomer(fullName: String, phoneNumber: String, email: String) {
        // Do save
        val customer: Customer? = customerRepository.getById(AuthenticatedUser.getInstance().getUserId())

        if (customer != null) {
            val updatedCustomer = customer.copy(fullName = fullName, phoneNo = phoneNumber, email = email)
            val updated = customerRepository.update(updatedCustomer)
            if (updated) {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.ProfileSaved, fullName = fullName, phoneNumber = phoneNumber, email = email))
            } else {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Could not update profile"))
            }
        } else {
            _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "You are not signed in."))
        }
    }

    private fun saveEmployee(fullName: String, phoneNumber: String, email: String) {
        // Do save
        val employee: Employee? = employeeRepository.getById(AuthenticatedUser.getInstance().getUserId())

        if (employee != null) {
            val updatedEmployee = employee.copy(fullName = fullName, phoneNo = phoneNumber, email = email)
            val updated = employeeRepository.update(updatedEmployee)
            if (updated) {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.ProfileSaved, fullName = fullName, phoneNumber = phoneNumber, email = email))
            } else {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Could not update profile"))
            }
        } else {
            _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "You are not signed in."))
        }
    }

    private fun refreshCustomer() {
        // Get the customer
        val customer: Customer? =
            customerRepository.getById(AuthenticatedUser.getInstance().getUserId())

        if (customer != null) {
            _uiState.value =
                _uiState.value?.copy(
                    loading = false,
                    fullName = customer.fullName ?: "",
                    phoneNumber = customer.phoneNo ?: "",
                    email = customer.email ?: ""
                )
        } else {
            _uiState.postValue(
                _uiState.value?.copy(
                    loading = false,
                    errorMessage = "Failed to load user"
                )
            )
        }
    }

    private fun refreshEmployee() {
        // Get the customer
        val employee: Employee? =
            employeeRepository.getById(AuthenticatedUser.getInstance().getUserId())

        if (employee != null) {
            _uiState.value =
                _uiState.value?.copy(
                    loading = false,
                    fullName = employee.fullName ?: "",
                    phoneNumber = employee.phoneNo ?: "",
                    email = employee.email ?: ""
                )
        } else {
            _uiState.postValue(
                _uiState.value?.copy(
                    loading = false,
                    errorMessage = "Failed to load user"
                )
            )
        }
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
        val fullName: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        var emailValidated: ValidatedResult = ValidatedResult(true,null),
        val fullNameValidated: ValidatedResult = ValidatedResult(true, null),
        val phoneNumberValidated: ValidatedResult = ValidatedResult(true, null)
    )

    /**
     * A type for sending events to the View Layer.
     */
    sealed interface Event {
        data object ProfileSaved: Event
    }
}