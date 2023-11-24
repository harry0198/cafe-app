package me.harrydrummond.cafeapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.CustomerRepository
import me.harrydrummond.cafeapplication.data.repository.EmployeeRepository
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.model.Role

class LoginViewModel(private val application: Application): AndroidViewModel(application) {

    private lateinit var userRepository: UserRepository
    val loginState: MutableLiveData<LoginAction> = MutableLiveData<LoginAction>(LoginAction.NONE)

    fun setUserLoginType(role: Role) {
        userRepository = when (role) {
            Role.CUSTOMER -> CustomerRepository(application)
            Role.EMPLOYEE -> EmployeeRepository(application)
        }
    }

    fun login(email: String, password: String) {
        loginState.value = LoginAction.PROGRESS

        viewModelScope.launch {
            val result = userRepository.loginUser(email, password)
            if (result == -1L) {
                loginState.value = LoginAction.EMAIL_OR_PASS_INVALID
                return@launch
            }
            val user = userRepository.getUserById(result)

            if (user == null) {
                loginState.value = LoginAction.ERROR
                return@launch
            }

            AuthenticatedUser.getInstance().user = user
            loginState.value = LoginAction.LOGIN_SUCCESS
        }
    }
}

enum class LoginAction {
    LOGIN_SUCCESS,
    PROGRESS,
    EMAIL_OR_PASS_INVALID,
    ERROR,
    NONE
}