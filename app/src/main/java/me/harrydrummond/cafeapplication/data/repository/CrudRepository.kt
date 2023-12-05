package me.harrydrummond.cafeapplication.data.repository

interface CrudRepository<T> {
    fun save(type: T): Int
    fun update(type: T): Boolean
    fun getById(id: Int): T?
    fun delete(type: T): Boolean
}