package me.harrydrummond.cafeapplication.data.model

import java.util.Date

data class Order(
    val orderId: Int,
    val timestamp: Date,
    val status: Status,
    val userId: Int
)
