package me.harrydrummond.cafeapplication.data.repository

import android.database.sqlite.SQLiteDatabase

interface Repository {

    fun create(sql: SQLiteDatabase)
    fun update(sql: SQLiteDatabase)
}