package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Cart
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

    /**
     * Logs the user into their account
     *
     * @param userId Id of the user logged in
     * @param role Role of the user logged in
     */
    fun login(userId: Int, role: Role) {
        Cart.getInstance().clear()
        this.userId = userId
        this.role = role
    }

    /**
     * Gets the currently logged in user id. -1 if not logged in.
     *
     * @return userid or -1
     */
    fun getUserId(): Int {
        return userId
    }

    /**
     * Sets the currently logged inn user role.
     *
     * @param role Role of user
     */
    fun setUserRole(role: Role) {
        this.role = role
    }

    /**
     * Gets the currently logged in user role or null.
     *
     * @return Nullable role
     */
    fun getUserRole(): Role? {
        return role
    }

    /**
     * Logs the current user out.
     */
    fun logout() {
        Cart.getInstance().clear()
        userId = -1
        role = null
    }
}