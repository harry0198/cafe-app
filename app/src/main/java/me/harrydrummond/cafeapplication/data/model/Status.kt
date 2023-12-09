package me.harrydrummond.cafeapplication.data.model

/**
 * Indicators of an order status
 *
 * @see Order
 */
enum class Status {
    COLLECTED,
    READY,
    PREPARING,
    RECEIVED,
    NONE
}