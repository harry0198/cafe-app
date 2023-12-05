package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.repository.contract.CustomerContract

/**
 * An SQLite Repository for the customer.
 *
 * @see AbstractSQLiteUserRepository
 * @see Customer
 * @see CustomerContract
 */
class SQLiteCustomerRepository(helper: SQLiteOpenHelper): AbstractSQLiteUserRepository<Customer>(helper, CustomerContract)