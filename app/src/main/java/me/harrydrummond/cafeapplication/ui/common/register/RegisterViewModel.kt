package me.harrydrummond.cafeapplication.ui.common.register

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

        userRepository.registerUser(email, password, UserModel("","Harry")).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val model = UserModel("null", "")
                userRepository.saveUser(Firebase.auth.currentUser!!, model).addOnCompleteListener {
                    if (it.isSuccessful) {
                        progress.value = RegisterAction.REGISTER_SUCCESS
                    } else {
                        progress.value = RegisterAction.FAILURE
                    }
                }

            } else {
                progress.value = RegisterAction.FAILURE
            }
        }


    }
}

enum class RegisterAction {
    REGISTER_SUCCESS,
    PROGRESS,
    FAILURE,
    NONE
}