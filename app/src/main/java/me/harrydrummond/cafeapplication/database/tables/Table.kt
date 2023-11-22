package me.harrydrummond.cafeapplication.database.tables

import android.database.sqlite.SQLiteDatabase

interface Table {
    fun onCreate(sql: SQLiteDatabase)
    fun onUpgrade(sql: SQLiteDatabase, oldVer: Int)
}