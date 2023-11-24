package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.model.UserModel
import java.util.concurrent.locks.ReentrantLock

class AuthenticatedUser private constructor() {

    private val lock = ReentrantLock()

    @Volatile
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

    // Use this function to temporarily block updates to the user property
    fun lockUserUpdate(block: () -> Unit) {
        lock.lock()
        try {
            block()
        } finally {
            lock.unlock()
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