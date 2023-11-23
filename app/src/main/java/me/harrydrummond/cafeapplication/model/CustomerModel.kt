package me.harrydrummond.cafeapplication.model

import java.io.Serializable

data class CustomerModel(
    val email: String,
    var password: String,
    var phoneNumber: Int,
    var fullName: String,
    var isActive: Boolean
): Serializable {
}