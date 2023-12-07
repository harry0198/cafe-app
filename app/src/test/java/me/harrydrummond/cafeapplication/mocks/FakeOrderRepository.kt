package me.harrydrummond.cafeapplication.mocks

import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository

class FakeOrderRepository: IOrderRepository {

    private val orders: MutableList<Order> = mutableListOf()

    /**
     * @inheritDoc
     */
    override fun getOrdersByUserId(userId: Int): List<Order> {
        return orders.filter { it.userId == userId }
    }

    /**
     * @inheritDoc
     */
    override fun getAllOrders(): List<Order> {
        return orders
    }

    /**
     * @inheritDoc
     */
    override fun save(type: Order): Int {
        val id = (1..100).random()
        val order = type.copy(orderId = id)
        orders.add(order)
        return id
    }

    /**
     * @inheritDoc
     */
    override fun update(type: Order): Boolean {
        val order = getById(type.orderId) ?: return false
        orders.remove(order)
        orders.add(type)
        return true
    }

    /**
     * @inheritDoc
     */
    override fun getById(id: Int): Order? {
        return orders.find { it.orderId == id }
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: Order): Boolean {
        return orders.remove(type)
    }
}