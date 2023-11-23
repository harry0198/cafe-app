package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class ProductTable : Table {

    companion object {
        const val TABLE_NAME = "product"
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val PRODUCT_PRICE = "product_price"
        const val PRODUCT_IMAGE = "product_image"
        const val PRODUCT_DESCRIPTION = "product_description"
        const val PRODUCT_AVAILABLE = "product_available"
    }

    override fun onCreate(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PRODUCT_NAME TEXT, " +
                "$PRODUCT_PRICE NUMERIC, " +
                "$PRODUCT_DESCRIPTION TEXT, " +
                "$PRODUCT_IMAGE TEXT, " +
                "$PRODUCT_AVAILABLE BOOLEAN)"

        sql.execSQL(createTableStatement)
    }

    override fun onUpgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }
}