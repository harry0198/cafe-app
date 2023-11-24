package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class OrderTable : Table {
    companion object {
        const val TABLE_NAME = "order_tbl"
        const val ORDER_ID = "order_id"
        const val USER_ID = "user_id"
        const val ORDER_DATE = "order_date"
        const val ORDER_STATUS_ID = "order_status_id"
    }

    override fun create(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($ORDER_ID PRIMARY KEY AUTOINCREMENT, " +
                "$USER_ID INTEGER, " +
                "$ORDER_DATE DATETIME, " +
                "$ORDER_STATUS_ID INTEGER)"

        sql.execSQL(createTableStatement)
    }

    override fun upgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }


}