package me.harrydrummond.cafeapplication.logic

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Status

/**
 * Helper class for displaying post notifications to the user
 *
 * @param context Context to use to create the notifications
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "order_status"
    }

    private val notificationManager: NotificationManager = context.getSystemService() ?: throw IllegalStateException()

    /**
     * Sets up the notification channels so that notifications can be
     * posted from this application.
     */
    fun setUpNotificationChannels() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Order Update",
                    // The importance must be IMPORTANCE_HIGH to show Bubbles.
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Description"
                }
            )
        }
    }

    /**
     * Posts a notification to the user with a given order status update.
     *
     * @param status Status of the order to show.
     */
    @WorkerThread
    fun showOrderStatusNotification(status: Status) {

        // Get notificaiton text
        val text: String
        when (status) {
            Status.PREPARING -> text = "being prepared"
            Status.READY -> text = "ready"
            else -> text = "placed"
        }

        // Create notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Order Status Update")
            .setContentText("Your order is now $text.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.cart_icon)
            .build()

        // Check for permissions and post notification.
        with (NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("NotificationHelper", "User has not given us permissions :(")
                return
            }
            notify(1, notification)
        }

    }
}