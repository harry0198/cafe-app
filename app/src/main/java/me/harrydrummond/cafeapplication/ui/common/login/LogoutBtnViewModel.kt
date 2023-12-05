package me.harrydrummond.cafeapplication.ui.common.login

import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.repository.IUserRepository

class LogoutBtnViewModel : ViewModel() {
    private val userRepository: IUserRepository = FirestoreUserRepository()


    /**
     * Logs the user out of their account
     */
    fun logoutUser() {
        userRepository.logoutUser()
    }
}