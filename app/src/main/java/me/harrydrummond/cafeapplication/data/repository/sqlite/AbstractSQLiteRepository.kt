package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.repository.CrudRepository
import me.harrydrummond.cafeapplication.data.repository.contract.BaseContract

/**
 * Abstract class with implementations of CRUD operations for an SQLite database.
 *
 * @param helper [SQLiteOpenHelper] to perform operations on.
 * @param contract [BaseContract] contract with specifications for repository.
 * @param T Generic data object type.
 */
abstract class AbstractSQLiteRepository<T>(
    private val helper: SQLiteOpenHelper,
    private val contract: BaseContract<T>
) : CrudRepository<T> {

    /**
     * Writes [T] values to a [ContentValues] object
     * @param type [T] to write values of
     * @param withPrimaryKey Should we write the primary key to the values. (useful for add where it's not needed)
     * @return [ContentValues] written values.
     */
    fun writeContentValues(type: T, withPrimaryKey: Boolean): ContentValues {
        return contract.getEntityValues(type, withPrimaryKey)
    }

    /**
     * @inheritDoc
     */
    override fun save(type: T): Int {
        val db = helper.writableDatabase
        val cv = writeContentValues(type, false)

        val success = db.insert(contract.TABLE_NAME, null, cv)

        db.close()
        return if (success.toInt() == -1) success.toInt() else success.toInt()
    }

    /**
     * @inheritDoc
     */
    override fun update(type: T): Boolean {
        val db = helper.writableDatabase
        val cv = writeContentValues(type, true)

        val result = db.update(contract.TABLE_NAME, cv, "${contract.ID} = ${contract.getId(type)}", null) == 1
        db.close()
        return result
    }

    /**
     * @inheritDoc
     */
    override fun getById(id: Int): T? {
        val sqlStatement = "${contract.ID} = ?"

        return getAllByQuery(sqlStatement,id.toString()).firstOrNull()
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: T): Boolean {
        val db = helper.writableDatabase
        val result = db.delete(contract.TABLE_NAME, "${contract.ID} = ?", arrayOf(contract.getId(type).toString())) == 1

        db.close()
        return result
    }

    /**
     * Gets all [T] by a given query.
     * @param query Query to run
     * @param args Arguments in query
      */
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