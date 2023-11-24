package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

interface Table {
    fun create(sql: SQLiteDatabase)
    fun upgrade(sql: SQLiteDatabase, oldVer: Int)
}