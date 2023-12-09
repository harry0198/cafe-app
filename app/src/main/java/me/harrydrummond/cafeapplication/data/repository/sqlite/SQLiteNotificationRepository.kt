package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.Notification
import me.harrydrummond.cafeapplication.data.repository.INotificationRepository
import me.harrydrummond.cafeapplication.data.repository.contract.NotificationContract

/**
 * Implementation of INotificationRepository for SQLite.
 */
class SQLiteNotificationRepository(helper: DataBaseHelper): AbstractSQLiteRepository<Notification>(helper, NotificationContract), INotificationRepository {

    /**
     * @inheritDoc
     */
    override fun notificationShown(notification: Notification) {
        delete(notification)
    }


    /**
     * @inheritDoc
     */
    override fun getNotificationsForUser(userId: Int): List<Notification> {
        val query = "${NotificationContract.USER_ID} = ?"

        return getAllByQuery(query, userId.toString())
    }
}