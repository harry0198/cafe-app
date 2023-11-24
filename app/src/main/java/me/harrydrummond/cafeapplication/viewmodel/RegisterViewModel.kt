package me.harrydrummond.cafeapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.CustomerRepository
import me.harrydrummond.cafeapplication.data.repository.EmployeeRepository
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.model.UserModel

class RegisterViewModel(private val application: Application): AndroidViewModel(application) {

    private lateinit var userRepository: UserRepository
    val progress: MutableLiveData<RegisterAction> = MutableLiveData<RegisterAction>(RegisterAction.NONE)

    fun setUserLoginType(role: Role) {
        userRepository = when (role) {
            Role.CUSTOMER -> CustomerRepository(application)
            Role.EMPLOYEE -> EmployeeRepository(application)
        }
    }

    fun register(email: String, password: String, fullname: String, phoneNumber: Int) {
        progress.value = RegisterAction.PROGRESS

        val customerModel = UserModel(-1, email, phoneNumber, fullname, password, true)

        if (emailExists(email)) {
            progress.value = RegisterAction.EMAIL_IN_USE
            return
        }
        val result = userRepository.registerUser(customerModel)

        if (result == -1L) {
            progress.value = RegisterAction.FAILURE
            return
        }

        val user = userRepository.getUserById(result)

        if (user == null) {
            progress.value = RegisterAction.FAILURE
            return
        }

        AuthenticatedUser.getInstance().user = user
        progress.value = RegisterAction.REGISTER_SUCCESS
    }

    private fun emailExists(email: String): Boolean {
        return userRepository.userExists(email)
    }
}

enum class RegisterAction {
    REGISTER_SUCCESS,
    PROGRESS,
    EMAIL_IN_USE,
    FAILURE,
    NONE
}