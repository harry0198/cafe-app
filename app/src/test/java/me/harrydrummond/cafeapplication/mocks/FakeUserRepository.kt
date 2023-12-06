package me.harrydrummond.cafeapplication.mocks

import me.harrydrummond.cafeapplication.data.model.User
import me.harrydrummond.cafeapplication.data.repository.IUserRepository

class FakeUserRepository<T: User>: IUserRepository<T> {

    private val users = mutableListOf<T>()

    /**
     * @inheritDoc
     */
    override fun getEntityIdByUsernameAndPassword(username: String, password: String): Int {
        val user = users.firstOrNull { (it.username == username && it.password == password) }
        return if (user != null) {
            user.id
        } else {
            -1
        }
    }

    /**
     * @inheritDoc
     */
    override fun getById(id: Int): T? {
        return users.firstOrNull { it.id == id }
    }

    /**
     * @inheritDoc
     */
    override fun update(type: T): Boolean {
        val user = getById(type.id)
        if (user != null) {
            users.remove(user)
            users.add(user)
            return true
        } else {
            return false
        }
    }

    /**
     * @inheritDoc
     */
    override fun save(type: T): Int {
        if (getEntityIdByUsernameAndPassword(type.username, type.password) != -1) {
            return -3
        }
        val newId = (1..1000).random()
        type.id = newId
        users.add(type)
        return newId
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: T): Boolean {
        val user = getById(type.id)
        return users.remove(user)
    }
}