package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class OrderStatusTable : Table {
    companion object {
        const val TABLE_NAME = "order_status"
        const val ORDER_STATUS_ID = "order_status_id"
        const val ORDER_STATUS = "order_status"
    }
    override fun create(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($ORDER_STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ORDER_STATUS TEXT)"

        sql.execSQL(createTableStatement)


    }

    override fun upgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }
}