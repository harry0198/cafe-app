package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.repository.CrudRepository
import me.harrydrummond.cafeapplication.data.repository.contract.BaseContract

abstract class AbstractSQLiteRepository<T>(
    private val helper: SQLiteOpenHelper,
    private val contract: BaseContract<T>
) : CrudRepository<T> {

    fun writeContentValues(type: T): ContentValues {
        return contract.getEntityValues(type)
    }

    override fun save(type: T): Int {
        val db = helper.writableDatabase
        val cv = writeContentValues(type)

        val success = db.insert(contract.TABLE_NAME, null, cv)

        db.close()
        return if (success.toInt() == -1) success.toInt() else success.toInt()
    }

    override fun update(type: T): Boolean {
        val db = helper.writableDatabase
        val cv = writeContentValues(type)

        val result = db.update(contract.TABLE_NAME, cv, "${contract.ID} = ${contract.getId(type)}", null) == 1
        db.close()
        return result
    }

    override fun getById(id: Int): T? {
        val db = helper.writableDatabase
        val sqlStatement = "SELECT * FROM ${contract.TABLE_NAME} WHERE ${contract.ID} = $id"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)
        val entity = if (cursor.moveToFirst()) {
            db.close()
            contract.toEntity(cursor)
        } else {
            db.close()
            null
        }

        cursor.close()
        db.close()
        return entity
    }

    override fun delete(type: T): Boolean {
        val db = helper.writableDatabase
        val result = db.delete(contract.TABLE_NAME, "${contract.ID} = ${contract.getId(type)}", null) == 1

        db.close()
        return result
    }

    fun getAllByQuery(query: String, contract: BaseContract<T>): List<T> {
        val db = helper.writableDatabase
        val list: MutableList<T> = mutableListOf()

        val cursor: Cursor =  db.rawQuery(query,null)
        while (cursor.moveToNext()) {
            list.add(contract.toEntity(cursor))
        }

        db.close()
        return list
    }
}