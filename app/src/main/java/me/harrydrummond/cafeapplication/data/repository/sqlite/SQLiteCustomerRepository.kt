package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.repository.PasswordUtils
import me.harrydrummond.cafeapplication.data.repository.contract.CustomerContract

/**
 * An SQLite Repository for the customer.
 *
 * @see AbstractSQLiteUserRepository
 * @see Customer
 * @see CustomerContract
 */
class SQLiteCustomerRepository(helper: SQLiteOpenHelper): AbstractSQLiteUserRepository<Customer>(helper, CustomerContract) {

    /**
     * @inheritDoc
     */
    override fun getEntityIdByUsernameAndPassword(username: String, password: String): Int {
        val entityId =  super.getEntityIdByUsernameAndPassword(username, password)
        val customer = super.getById(entityId) ?: return entityId

        if (PasswordUtils.verifyPassword(password, customer.password)) {
            return entityId
        } else {
            return -1
        }
    }
}