package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class OrderDetailsTable : Table {
    companion object {
        const val TABLE_NAME = "order_details"
        const val ORDER_DETAILS_ID = "order_details_id"
        const val ORDER_ID = "order_id"
        const val PRODUCT_ID = "product_id"
    }
    override fun onCreate(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($ORDER_DETAILS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ORDER_ID INTEGER, " +
                "$PRODUCT_ID INTEGER)"

        sql.execSQL(createTableStatement)
    }

    override fun onUpgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }
}