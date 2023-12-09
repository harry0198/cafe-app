package me.harrydrummond.cafeapplication.data.model

data class Notification(
    val notificationId: Int,
    val userId: Int,
    val notificationMessage: String,
)