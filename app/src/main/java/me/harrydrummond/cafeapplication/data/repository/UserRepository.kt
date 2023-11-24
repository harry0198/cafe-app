package me.harrydrummond.cafeapplication.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import me.harrydrummond.cafeapplication.data.DatabaseHelper
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.model.UserModel
import org.mindrot.jbcrypt.BCrypt

abstract class UserRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    protected abstract val tableName: String
    protected abstract val userId: String
    protected abstract val email: String
    protected abstract val fullname: String
    protected abstract val password: String
    protected abstract val phoneNumber: String
    protected abstract val isActive: String

    fun create(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $tableName" +
                "($userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$fullname TEXT, " +
                "$email TEXT, " +
                "$password TEXT, " +
                "$phoneNumber INTEGER, " +
                "$isActive BOOLEAN)"

        sql.execSQL(createTableStatement)
    }

    fun userExists(emailAddr: String): Boolean {
        // verify user not already exist
        val db = dbHelper.writableDatabase
        val query = "SELECT COUNT(*) FROM $tableName WHERE $email = ?"

        val cursor: Cursor = db.rawQuery(query, arrayOf(emailAddr))
        cursor.moveToFirst()

        val count = cursor.getInt(0)
        cursor.close()

        return count > 0
    }

    fun registerUser(customer: UserModel): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(email, customer.email)
            put(password, hashPassword(customer.password!!))
            put(phoneNumber, customer.phoneNumber)
            put(fullname, customer.fullName)
            put(isActive, true)
        }

        val userId = db.insert(tableName, null, values)
        db.close()

        return userId
    }

    fun loginUser(email: String, password: String): Long {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM $tableName WHERE $email = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, arrayOf(email))

            if (!cursor.moveToFirst()) return -1

            val userIdIndex = cursor.getColumnIndex(userId)
            val passwordIndex = cursor.getColumnIndex(password)

            if (passwordIndex == -1 || userIdIndex == -1) {
                Log.d("UserRepository:loginUser", "tried to fetch missing column.")
                return -1
            }

            val hashedPassword = cursor.getString(passwordIndex)

            if (verifyPassword(password, hashedPassword)) {
                return cursor.getLong(userIdIndex)
            }
        } finally {
            cursor?.close()
            db.close()
        }


        return -1
    }

    @SuppressLint("Range")
    fun getUserById(id: Long): UserModel? {
        val db = dbHelper.readableDatabase
        val selection = "$userId = ?"
        val selectionArgs = arrayOf(id.toString())
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                tableName,  // Table name
                arrayOf("*"),  // Columns to retrieve
                selection,  // WHERE clause
                selectionArgs,  // Arguments for WHERE clause
                null,  // GROUP BY
                null,  // HAVING
                null // ORDER BY
            )

            val user: UserModel?

            if (cursor != null && cursor.moveToFirst()) {
                // Assuming User is a class with appropriate constructor
                user = UserModel(
                    cursor.getLong(cursor.getColumnIndex(userId)),
                    cursor.getString(cursor.getColumnIndex(email)),
                    cursor.getInt(cursor.getColumnIndex(phoneNumber)),
                    cursor.getString(cursor.getColumnIndex(fullname)),
                    cursor.getString(cursor.getColumnIndex(password)),
                    cursor.getInt(cursor.getColumnIndex(isActive)) == 1,
                    getRole()
                )

                return user
            }
        } finally {
            cursor?.close()
            db.close()
        }

        return null
    }

    abstract fun getRole(): Role

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }

}