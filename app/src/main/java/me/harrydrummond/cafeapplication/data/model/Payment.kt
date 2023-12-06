package me.harrydrummond.cafeapplication.data.model

import java.util.Date

data class Payment(
    val paymentId: Int,
    val orderId: Int,
    val paymentType: String,
    val amount: Double,
    val paymentDate: Date
) {
}