package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product

/**
 * Interface defining signatures to perform CRUD operations on an order repository.
 *
 * @see Order
 * @see Task
 */
interface IOrderRepository {

    /**
     * Saves an order to the database or updates its fields if it already exists.
     *
     * @param order Order to save to the database
     * @return Task defining if the process was successful or not.
     *
     * @see Task
     * @see Order
     */
    fun saveOrder(order: Order): Task<Void>

    /**
     * Fetches an order object from the database if one exists and can map to an Order object.
     *
     * @param orderId Id of the order to fetch
     * @return Task holding a nullable order.
     *
     * @see Order
     * @see Task
     */
    fun getOrder(orderId: String): Task<Order?>

    /**
     * Fetches all orders from the database and maps them to an Order object.
     *
     * @return Task holding a list of orders
     *
     * @see Order
     * @see Task
     */
    fun getOrders(): Task<List<Order>>

    /**
     * Loads the ProductModels assigned to an order with their quantities in a pair.
     *
     * @param order Order to load the product and quantities of. First is quantity, Second is
     * ProductModel.
     *
     * @see Order
     * @see Task
     * @see Pair
     * @see Product
     */
    fun fullLoadOrderProducts(order: Order): Task<List<Pair<Int, Product>>?>

    /**
     * Fetches all the orders from the currently logged-in user.
     *
     * @return Task containing a list of orders. List will be empty if none found or user is null.
     *
     * @see Order
     * @see Task
     */
    fun getOrdersByUser(): Task<List<Order>>
}