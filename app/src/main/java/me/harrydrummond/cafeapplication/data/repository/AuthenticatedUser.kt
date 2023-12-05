package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Role

/**
 * Singleton for the currently logged in user.
 */
class AuthenticatedUser private constructor() {

    private var userId: Int = -1
    private var role: Role? = null

    companion object {
        // Singleton instance
        private var instance: AuthenticatedUser? = null

        // Thread-safe getInstance using the double-checked locking pattern
        fun getInstance(): AuthenticatedUser {
            return instance ?: synchronized(this) {
                instance ?: AuthenticatedUser().also { instance = it }
            }
        }
    }

    fun setUserId(userId: Int) {
        this.userId = userId
    }

    fun getUserId(): Int {
        return userId
    }

    fun setUserRole(role: Role) {
        this.role = role
    }

    fun getUserRole(): Role? {
        return role
    }
}