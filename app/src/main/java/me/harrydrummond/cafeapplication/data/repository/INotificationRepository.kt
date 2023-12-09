package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Notification

interface INotificationRepository: CrudRepository<Notification> {

    /**
     * Notification was shown.
     *
     * @param notification Notification to indicate as shown.
     */
    fun notificationShown(notification: Notification)

    /**
     * Gets all notifications for the user
     *
     * @param userId Id of the user to get notificatons of
     * @return List of notifications for user.
     */
    fun getNotificationsForUser(userId: Int): List<Notification>
}