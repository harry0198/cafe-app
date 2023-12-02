package me.harrydrummond.cafeapplication.ui.common.login

import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
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