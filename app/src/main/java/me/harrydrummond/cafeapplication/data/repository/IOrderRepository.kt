package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Order

/**
 * Interface defining signatures to perform CRUD operations on an order repository.
 *
 * @see Order
 */
interface IOrderRepository: CrudRepository<Order> {

    fun getOrdersByUserId(userId: Int): List<Order>

    fun getAllOrders(): List<Order>
}