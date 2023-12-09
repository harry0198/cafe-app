package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Notification
import me.harrydrummond.cafeapplication.data.repository.toNotification

object NotificationContract : BaseContract<Notification> {
    const val USER_ID = "user_id"
    const val MESSAGE = "message"

    override val TABLE_NAME = "Notification"
    override val ID = "NotificationId"
    override val CREATE_TABLE =
        "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID INTEGER NOT NULL, " +
                "$MESSAGE TEXT NOT NULL )"



    override fun getEntityValues(entity: Notification, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.notificationId)
        cv.put(
            USER_ID, entity.userId)
        cv.put(MESSAGE, entity.notificationMessage)
        return cv
    }

    override fun getId(entity: Notification): Int = entity.notificationId

    override fun toEntity(cursor: Cursor): Notification {
        return cursor.toNotification()
    }
}