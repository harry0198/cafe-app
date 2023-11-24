package me.harrydrummond.cafeapplication.model

import java.io.Serializable

data class UserModel(
    val userId: Long,
    val email: String,
    var phoneNumber: Int,
    var fullName: String,
    var password: String?,
    var isActive: Boolean,
    var role: Role = Role.CUSTOMER
): Serializable {
}