package me.harrydrummond.cafeapplication.data.model

import java.io.Serializable

data class UserModel(
    var phoneNumber: String = "",
    var fullName: String = "",
    var role: Role = Role.CUSTOMER
): Serializable {
}