package me.harrydrummond.cafeapplication.data.model

/**
 * Employee data class
 *
 * @param id Id in database
 * @param fullName of Employee
 * @param email of Employee
 * @param phoneNo of Employee in string format
 * @param username of Employee
 * @param password of Employee
 * @param isActive Is Employee account activated
 */
data class Employee(
    override val id: Int,
    val fullName: String? = null,
    val email: String? = null,
    val phoneNo: String? = null,
    override val username: String,
    override val password: String,
    val isActive: Boolean):
    User(id, username, password)