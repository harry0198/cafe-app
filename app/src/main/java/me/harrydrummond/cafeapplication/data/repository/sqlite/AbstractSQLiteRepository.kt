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

    fun writeContentValues(type: T, withPrimaryKey: Boolean): ContentValues {
        return contract.getEntityValues(type, withPrimaryKey)
    }

    override fun save(type: T): Int {
        val db = helper.writableDatabase
        val cv = writeContentValues(type, false)

        val success = db.insert(contract.TABLE_NAME, null, cv)

        db.close()
        return if (success.toInt() == -1) success.toInt() else success.toInt()
    }

    override fun update(type: T): Boolean {
        val db = helper.writableDatabase
        val cv = writeContentValues(type, true)

        val result = db.update(contract.TABLE_NAME, cv, "${contract.ID} = ${contract.getId(type)}", null) == 1
        db.close()
        return result
    }

    override fun getById(id: Int): T? {
        val sqlStatement = "${contract.ID} = ?"

        return getAllByQuery(sqlStatement,id.toString()).firstOrNull()
    }

    override fun delete(type: T): Boolean {
        val db = helper.writableDatabase
        val result = db.delete(contract.TABLE_NAME, "${contract.ID} = ${contract.getId(type)}", null) == 1

        db.close()
        return result
    }

    fun getAllByQuery(query: String?, args: String?): List<T> {
        val db = helper.writableDatabase
        val list: MutableList<T> = mutableListOf()

        val cursor = db.query(
            contract.TABLE_NAME,
            arrayOf("*"),
            query,
            if (args != null) arrayOf(args) else null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            list.add(contract.toEntity(cursor))
        }

        db.close()
        return list
    }
}