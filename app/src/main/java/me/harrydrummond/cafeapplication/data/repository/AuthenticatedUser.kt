package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.model.UserModel

class AuthenticatedUser private constructor() {

    var user: UserModel? = null

    // Standard singleton generation
    companion object {

        @Volatile
        private var instance: AuthenticatedUser? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AuthenticatedUser().also { instance = it }
            }
    }

    /**
     * Is the user authenticated
     */
    fun isAuthenticated(): Boolean {
        return user != null
    }

    /**
     * Quality-of-life function to check if authenticated user is
     * an employee.
     */
    fun isEmployee(): Boolean {
        return user?.role == Role.EMPLOYEE
    }

    /**
     * Quality-of-life function to check if authenticated user is
     * a customer.
     */
    fun isCustomer(): Boolean {
        return user?.role == Role.CUSTOMER
    }
}