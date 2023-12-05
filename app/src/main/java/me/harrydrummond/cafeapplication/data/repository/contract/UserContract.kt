package me.harrydrummond.cafeapplication.data.repository.contract

interface UserContract<T>: BaseContract<T> {
    fun getUsername(): String
    fun getPassword(): String
}