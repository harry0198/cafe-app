package me.harrydrummond.cafeapplication.data.model

import java.util.Date

/**
 * Data class indicating a payment method and amount
 *
 * @param paymentId Id of entry
 * @param orderId Id of the order
 * @param paymentType Type of payment e.g "CARD"
 * @param paymentDate Date of payment
 * @param amount Amount paid
 */
data class Payment(
    val paymentId: Int,
    val orderId: Int,
    val paymentType: String,
    val amount: Double,
    val paymentDate: Date
) {
}