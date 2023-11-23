package me.harrydrummond.cafeapplication.data.tables

import android.database.sqlite.SQLiteDatabase

interface Table {
    fun onCreate(sql: SQLiteDatabase)
    fun onUpgrade(sql: SQLiteDatabase, oldVer: Int)
}