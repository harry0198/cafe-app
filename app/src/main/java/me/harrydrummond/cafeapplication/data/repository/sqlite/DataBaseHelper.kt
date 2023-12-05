package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.data.repository.contract.CustomerContract
import me.harrydrummond.cafeapplication.data.repository.contract.EmployeeContract
import me.harrydrummond.cafeapplication.data.repository.contract.ProductContract

private const val DataBaseName = "Database.db"
private const val ver : Int = 1
class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

    val customerRepository: IUserRepository<Customer> = SQLiteCustomerRepository(this)
    val productRepository: IProductRepository = SQLiteProductRepository(this)
    val employeeRepository: IUserRepository<Employee> = SQLiteEmployeeRepository(this)

    override fun onCreate(db: SQLiteDatabase?) {
        val database = db ?: return

        try {
            // Create tables
            database.execSQL(CustomerContract.CREATE_TABLE)
            database.execSQL(ProductContract.CREATE_TABLE)
            database.execSQL(EmployeeContract.CREATE_TABLE)
        } catch (e: SQLiteException) {

        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}