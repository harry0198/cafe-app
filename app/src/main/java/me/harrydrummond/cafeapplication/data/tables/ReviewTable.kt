package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

class ReviewTable : Table {

    companion object {
        const val TABLE_NAME = "reviews"
        const val REVIEW_ID = "review_id"
        const val PRODUCT_ID = "product_id"
        const val RATING = "rating"
        const val REVIEW = "review"
    }

    override fun onCreate(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($REVIEW_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PRODUCT_ID INTEGER, " +
                "$RATING TINYINT, " +
                "$REVIEW TEXT)"

        sql.execSQL(createTableStatement)
    }

    override fun onUpgrade(sql: SQLiteDatabase, oldVer: Int) {
        TODO("Not yet implemented")
    }
}