package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor

/**
 * A contract that specifies table name, primary key id and the create table statement.
 *
 * Also contains methods to get content values from an entity, get ID and convert a cursor to the
 * contracted object.
 */
interface BaseContract<T> {
    val TABLE_NAME: String
    val ID: String
    val CREATE_TABLE: String

    fun getEntityValues(entity: T): ContentValues
    fun getId(entity: T): Int
    fun toEntity(cursor: Cursor): T
}