package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class AdministratorTable : Table {

    companion object {
        const val TABLE_NAME = "customer"
        const val USER_ID = "user_id"
        const val EMAIL = "email"
        const val FULL_NAME = "full_name"
        const val PHONE_NUMBER = "phone_number"
        const val PASSWORD = "password"
        const val IS_ACTIVE = "is_active"
    }

    override fun onCreate(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME" +
                "($USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$FULL_NAME TEXT, " +
                "$EMAIL TEXT, " +
                "$PASSWORD TEXT, " +
                "$PHONE_NUMBER INTEGER, " +
                "$IS_ACTIVE BOOLEAN)"

        sql.execSQL(createTableStatement)
    }

    override fun onUpgrade(sql: SQLiteDatabase, oldVer: Int) {
        // Nothing to upgrade
    }
}