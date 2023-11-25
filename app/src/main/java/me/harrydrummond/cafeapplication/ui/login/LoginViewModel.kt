package me.harrydrummond.cafeapplication.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel

class LoginViewModel(private val application: Application): AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository()
    val loginState: MutableLiveData<LoginAction> = MutableLiveData<LoginAction>(LoginAction.NONE)


    fun login(email: String, password: String) {
        loginState.value = LoginAction.PROGRESS
        val result = userRepository.loginUser(email, password)

        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginState.value = LoginAction.LOGIN_SUCCESS
            } else {
                loginState.value = LoginAction.ERROR
            }
        }
    }

    fun getUser(): Task<UserModel?> {
        return userRepository.getUser(Firebase.auth.uid!!)
    }
}

enum class LoginAction {
    LOGIN_SUCCESS,
    PROGRESS,
    EMAIL_OR_PASS_INVALID,
    ERROR,
    NONE
}