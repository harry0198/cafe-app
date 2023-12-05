package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.contract.EmployeeContract

/**
 * An SQLite Repository for the employee.
 *
 * @see AbstractSQLiteUserRepository
 * @see Employee
 * @see EmployeeContract
 */
class SQLiteEmployeeRepository(helper: SQLiteOpenHelper): AbstractSQLiteUserRepository<Employee>(helper, EmployeeContract)