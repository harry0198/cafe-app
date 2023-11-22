package me.harrydrummond.cafeapplication.database.tables

import android.database.sqlite.SQLiteDatabase

class PaymentTable : Table {

    companion object {
        const val TABLE_NAME = "payment"
        const val PAYMENT_ID = "payment_id"
        const val ORDER_ID = "order_id"
        const val PAYMENT_TYPE = "payment_type"
        const val AMOUNT = "amount"
        const val PAYMENT_DATE = "payment_date"
    }
    override fun onCreate(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($PAYMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ORDER_ID INTEGER, " +
                "$PAYMENT_TYPE TEXT, " +
                "$AMOUNT NUMERIC," +
                "$PAYMENT_DATE DATETIME)"

        sql.execSQL(createTableStatement)
    }

    override fun onUpgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }
}