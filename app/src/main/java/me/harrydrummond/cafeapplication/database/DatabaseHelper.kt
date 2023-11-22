package me.harrydrummond.cafeapplication.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.database.tables.AdministratorTable
import me.harrydrummond.cafeapplication.database.tables.CustomerTable
import me.harrydrummond.cafeapplication.database.tables.OrderDetailsTable
import me.harrydrummond.cafeapplication.database.tables.OrderStatusTable
import me.harrydrummond.cafeapplication.database.tables.OrderTable
import me.harrydrummond.cafeapplication.database.tables.PaymentTable
import me.harrydrummond.cafeapplication.database.tables.ProductTable
import me.harrydrummond.cafeapplication.database.tables.ReviewTable

class DatabaseHelper(ctx: Context) : SQLiteOpenHelper(ctx, "Cafe.db", null, 1) {

    override fun onCreate(sql: SQLiteDatabase) {
        AdministratorTable().onCreate(sql)
        CustomerTable().onCreate(sql)
        OrderDetailsTable().onCreate(sql)
        OrderStatusTable().onCreate(sql)
        OrderTable().onCreate(sql)
        PaymentTable().onCreate(sql)
        ProductTable().onCreate(sql)
        ReviewTable().onCreate(sql)

        // Foreign Keys
        addForeignKey(sql, OrderTable.TABLE_NAME, CustomerTable.TABLE_NAME, OrderTable.USER_ID, CustomerTable.USER_ID)
        addForeignKey(sql, PaymentTable.TABLE_NAME, OrderTable.TABLE_NAME, PaymentTable.ORDER_ID, OrderTable.ORDER_ID)
        addForeignKey(sql, OrderTable.TABLE_NAME, OrderStatusTable.TABLE_NAME, OrderTable.ORDER_STATUS_ID, OrderStatusTable.ORDER_STATUS_ID)
        addForeignKey(sql, OrderDetailsTable.TABLE_NAME, OrderTable.TABLE_NAME, OrderDetailsTable.ORDER_ID, OrderTable.ORDER_ID)
        addForeignKey(sql, OrderDetailsTable.TABLE_NAME, ProductTable.TABLE_NAME, OrderDetailsTable.PRODUCT_ID, ProductTable.PRODUCT_ID)
        addForeignKey(sql, ReviewTable.TABLE_NAME, ProductTable.TABLE_NAME, ReviewTable.PRODUCT_ID, ProductTable.PRODUCT_ID)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    private fun addForeignKey(sql: SQLiteDatabase, table1: String, table2: String, field1: String, field2: String) {
        val statement = "ALTER TABLE $table1 ADD FOREIGN KEY ($field1) REFERENCES $table2($field2)"
        sql.execSQL(statement)
    }
}