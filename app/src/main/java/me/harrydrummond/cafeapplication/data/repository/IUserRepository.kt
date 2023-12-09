package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.User

/**
 * Interface defining signatures to perform CRUD operations on a user repository.
 *
 * @see User
 * @see CrudRepository
 */
interface IUserRepository<T: User>: CrudRepository<T> {

    /**
     * Get an entity's id by the user login and password.
     *
     * @param username Username to match
     * @param password Password to match
     *
     * @return User id or -1 if not found.
     */
    fun getEntityIdByUsernameAndPassword(username: String, password: String): Int

    /**
     * Gets all user ids in the database
     *
     * @return List of user ids.
     */
    fun getAllUserIds(): List<Int>
}