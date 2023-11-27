package me.harrydrummond.cafeapplication.ui.common.register.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.UserRepository

class CreateProfileViewModel: ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    val saveUIState: MutableLiveData<State> = MutableLiveData(State.NONE)

    fun saveProfileInformation(firstName: String, lastName: String, phoneNumber: String) {
        saveUIState.value = State.PROCESSING

        val model = UserModel(phoneNumber, "$firstName $lastName")
        Firebase.auth.currentUser?.let {
            userRepository.saveUser(it, model).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUIState.value = State.COMPLETE_SUCCESS
                } else {
                    saveUIState.value = State.COMPLETE_FAILURE
                }
            }
        }
    }
}

enum class State {
    PROCESSING,
    COMPLETE_SUCCESS,
    COMPLETE_FAILURE,
    NONE
}