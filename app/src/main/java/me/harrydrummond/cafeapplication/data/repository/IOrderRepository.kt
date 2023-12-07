package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Order

/**
 * Interface defining signatures to perform CRUD operations on an order repository.
 *
 * @see Order
 */
interface IOrderRepository: CrudRepository<Order> {

    /**
     * Gets all orders owned by a user by id.
     *
     * @param userId Id of the user
     * @return All found orders as list
     *
     * @see Order
     */
    fun getOrdersByUserId(userId: Int): List<Order>

    /**
     * Gets all orders in database
     *
     * @return All found orders as list
     *
     * @see Order
     */
    fun getAllOrders(): List<Order>
}