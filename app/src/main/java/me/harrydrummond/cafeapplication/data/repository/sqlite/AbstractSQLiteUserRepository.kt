package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.User
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.data.repository.contract.UserContract

/**
 * Creates an abstract user repository where the type MUST be of type user.
 *
 * @see AbstractSQLiteRepository
 * @see User
 */
abstract class AbstractSQLiteUserRepository<T: User>(
    private val helper: SQLiteOpenHelper,
    private val contract: UserContract<T>
) : AbstractSQLiteRepository<T>(helper, contract), IUserRepository<T> {

    /**
     * @inheritDoc
     */
    override fun getEntityIdByUsernameAndPassword(username: String, password: String): Int {
        val db = helper.writableDatabase
        val userName = username.lowercase()

        val sqlStatement = "SELECT * FROM ${contract.TABLE_NAME} WHERE ${contract.getUsername()} = ? AND ${contract.getPassword()} = ?"
        val param = arrayOf(userName, password)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        val entityId = if (cursor.moveToFirst()) {
            val n = cursor.getInt(0)
            cursor.close()
            n
        } else {
            cursor.close()
            -1 // User not found
        }

        db.close()
        return entityId
    }

    override fun save(type: T): Int {
        val isUserNameAlreadyExists = usernameExists(type)
        if (isUserNameAlreadyExists)
            return -3

        val db = helper.writableDatabase
        val cv = writeContentValues(type)

        val success = db.insert(contract.TABLE_NAME, null, cv)

        db.close()
        return if (success.toInt() == -1) success.toInt() else success.toInt()
    }

    private fun usernameExists(entity: T): Boolean {
        val db = helper.writableDatabase
        val userName = entity.username

        val sqlStatement = "SELECT * FROM ${contract.TABLE_NAME} WHERE ${contract.getUsername()} = ?"
        val param = arrayOf(userName)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }
}