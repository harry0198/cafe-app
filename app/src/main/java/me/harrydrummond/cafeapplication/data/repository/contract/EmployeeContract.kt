package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.toEmployee

/**
 * Contains the table name and columns for creating the sqlite database.
 * Also contains the create statement.
 */
object EmployeeContract : UserContract<Employee> {
    const val FULL_NAME = "AdminFullName"
    const val EMAIL = "AdminEmail"
    const val PHONE_NO = "AdminPhoneNo"
    const val USERNAME = "AdminUserName"
    const val PASSWORD = "AdminPassword"
    const val IS_ACTIVE = "AdminIsActive"

    override val TABLE_NAME = "Admin"
    override val ID = "AdminId"
    override val CREATE_TABLE =
        "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $FULL_NAME TEXT, " +
                "$EMAIL TEXT, $PHONE_NO TEXT, $USERNAME TEXT NOT NULL UNIQUE, $PASSWORD TEXT NOT NULL, $IS_ACTIVE INTEGER NOT NULL )"


    override fun getUsername(): String {
        return USERNAME
    }

    override fun getPassword(): String {
        return PASSWORD
    }

    override fun getEntityValues(entity: Employee, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.id)
        cv.put(FULL_NAME, entity.fullName)
        cv.put(EMAIL, entity.email)
        cv.put(PHONE_NO, entity.phoneNo)
        cv.put(USERNAME, entity.username)
        cv.put(PASSWORD, entity.password)
        cv.put(IS_ACTIVE, entity.isActive)
        return cv
    }

    override fun getId(entity: Employee): Int = entity.id

    override fun toEntity(cursor: Cursor): Employee {
        return cursor.toEmployee()
    }
}