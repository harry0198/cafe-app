package me.harrydrummond.cafeapplication.data.model

open class User(open var id: Int, open val username: String, open val password: String, open val isActive: Boolean) {
}