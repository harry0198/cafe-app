package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

data class UserModel(
    var phoneNumber: Int = 0,
    var fullName: String = "Harry",
    var active: Boolean = true,
    var role: Role = Role.CUSTOMER
): Serializable {
}