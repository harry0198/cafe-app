package me.harrydrummond.cafeapplication.data.repository

import android.content.Context
import me.harrydrummond.cafeapplication.model.Role

class CustomerRepository(context: Context) : UserRepository(context) {

    override val tableName = "customer"
    override val userId = "user_id"
    override val email = "email"
    override val phoneNumber = "phone_number"
    override val password = "password"
    override val isActive = "is_active"
    override val fullname = "full_name"

    override fun getRole(): Role {
        return Role.CUSTOMER
    }

}