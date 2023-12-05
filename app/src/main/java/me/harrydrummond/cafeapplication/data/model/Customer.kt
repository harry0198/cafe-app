package me.harrydrummond.cafeapplication.data.model

/**
 * Customer data class
 *
 * @param id Id in database
 * @param fullName of customer
 * @param email of customer
 * @param phoneNo of customer in string format
 * @param username of customer
 * @param password of customer
 * @param isActive Is customer account activated
 */
data class Customer(
    override val id: Int,
    val fullName: String? = null,
    val email: String? = null,
    val phoneNo: String? = null,
    override val username: String,
    override val password: String,
    val isActive: Boolean):
    User(id, username, password)