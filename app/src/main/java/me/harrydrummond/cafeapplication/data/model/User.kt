package me.harrydrummond.cafeapplication.data.model

/**
 * Base class that indicates a user who can login and register.
 *
 * @param id Id of the user in the database
 * @param username of the user
 * @param password of the user
 * @param isActive Is the user account activated?
 */
open class User(open var id: Int, open val username: String, open val password: String, open val isActive: Boolean) {
}