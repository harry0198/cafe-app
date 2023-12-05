package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.repository.toCustomer

/**
 * Contains the table name and columns for creating the sqlite database.
 * Also contains the create statement.
 */
object CustomerContract : UserContract<Customer> {

    const val FULL_NAME = "CusFullName"
    const val EMAIL = "CusEmail"
    const val PHONE_NO = "CusPhoneNo"
    const val USERNAME = "CusUserName"
    const val PASSWORD = "CusPassword"
    const val IS_ACTIVE = "CusIsActive"

    override val TABLE_NAME = "Customer"
    override val ID = "CusId"
    override val CREATE_TABLE =
        "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $FULL_NAME TEXT, " +
                "$EMAIL TEXT, $PHONE_NO TEXT, $USERNAME TEXT NOT NULL UNIQUE, $PASSWORD TEXT NOT NULL, $IS_ACTIVE INTEGER NOT NULL )"


    override fun getUsername(): String {
        return USERNAME
    }

    override fun getPassword(): String {
        return PASSWORD
    }

    override fun getEntityValues(entity: Customer, withPrimaryKey: Boolean): ContentValues {
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

    override fun getId(entity: Customer): Int = entity.id

    override fun toEntity(cursor: Cursor): Customer {
        return cursor.toCustomer()
    }
}