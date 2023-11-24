package me.harrydrummond.cafeapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.repository.CustomerRepository
import me.harrydrummond.cafeapplication.data.repository.EmployeeRepository
import me.harrydrummond.cafeapplication.data.repository.ProductRepository

class DatabaseHelper(val context: Context) : SQLiteOpenHelper(context, "Cafe.db", null, 1) {

    override fun onCreate(sql: SQLiteDatabase) {
        CustomerRepository(context).create(sql)
        EmployeeRepository(context).create(sql)
        ProductRepository(context).create(sql)

        // Foreign Keys
//        addForeignKey(sql, OrderTable.TABLE_NAME, CustomerRepository.TABLE_NAME, OrderTable.USER_ID, CustomerRepository.USER_ID)
//        addForeignKey(sql, PaymentTable.TABLE_NAME, OrderTable.TABLE_NAME, PaymentTable.ORDER_ID, OrderTable.ORDER_ID)
//        addForeignKey(sql, OrderTable.TABLE_NAME, OrderStatusTable.TABLE_NAME, OrderTable.ORDER_STATUS_ID, OrderStatusTable.ORDER_STATUS_ID)
//        addForeignKey(sql, OrderDetailsTable.TABLE_NAME, OrderTable.TABLE_NAME, OrderDetailsTable.ORDER_ID, OrderTable.ORDER_ID)
//        addForeignKey(sql, OrderDetailsTable.TABLE_NAME, ProductTable.TABLE_NAME, OrderDetailsTable.PRODUCT_ID, ProductTable.PRODUCT_ID)
//        addForeignKey(sql, ReviewTable.TABLE_NAME, ProductTable.TABLE_NAME, ReviewTable.PRODUCT_ID, ProductTable.PRODUCT_ID)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    private fun addForeignKey(sql: SQLiteDatabase, table1: String, table2: String, field1: String, field2: String) {
        val statement = "ALTER TABLE $table1 ADD FOREIGN KEY ($field1) REFERENCES $table2($field2)"
        sql.execSQL(statement)
    }
}