package me.harrydrummond.cafeapplication.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.data.model.UserModel

class RegisterViewModel: ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    val progress: MutableLiveData<RegisterAction> = MutableLiveData<RegisterAction>(RegisterAction.NONE)

    fun register(email: String, password: String) {
        progress.value = RegisterAction.PROGRESS

        userRepository.registerAndSaveUser(email, password, UserModel(333,"Harry", true)).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                progress.value = RegisterAction.REGISTER_SUCCESS
            } else {
                progress.value = RegisterAction.FAILURE
            }
        }
    }

    fun getUser(): Task<UserModel?> {
        return userRepository.getUser(Firebase.auth.uid!!)
    }
}

enum class RegisterAction {
    REGISTER_SUCCESS,
    PROGRESS,
    FAILURE,
    NONE
}