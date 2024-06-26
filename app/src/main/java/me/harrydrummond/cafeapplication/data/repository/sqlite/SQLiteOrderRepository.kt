package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.annotation.SuppressLint
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.OrderDetails
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.contract.OrderContract
import me.harrydrummond.cafeapplication.data.repository.contract.OrderDetailsContract
import me.harrydrummond.cafeapplication.data.repository.contract.ProductContract
import me.harrydrummond.cafeapplication.data.repository.toOrder
import me.harrydrummond.cafeapplication.data.repository.toProduct

/**
 * An sqlite repository for performing operations on the order repository.
 * Overrides methods for the joining of products.
 *
 * @param helper [DataBaseHelper] to perform operations on.
 */
class SQLiteOrderRepository(private val helper: DataBaseHelper): AbstractSQLiteRepository<Order>(helper, OrderContract),
    IOrderRepository {

    private val orderDetailsRepository = SQLiteOrderDetailsRepository(helper)

    /**
     * @inheritDoc
     */
    override fun update(type: Order): Boolean {
        // Update plain order without products
        val updated = super.update(type)

        if (!updated) return false

        // Delete all order detail relations
        val success = orderDetailsRepository.deleteByOrderId(type.orderId)

        saveProductsToOrder(type.products, type.orderId)

        return success
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: Order): Boolean {
        orderDetailsRepository.deleteByOrderId(type.orderId)
        return super.delete(type)
    }

    /**
     * @inheritDoc
     */
    override fun save(type: Order): Int {
        // Saves plain order without products
        val savedId = super.save(type)

        if (savedId == -1) return savedId

        saveProductsToOrder(type.products, savedId)
        return savedId
    }

    /**
     * @inheritDoc
     */
    override fun getOrdersByUserId(userId: Int): List<Order> {
        val query = "${OrderContract.CUSTOMER_ID} = ?"
        val orders = getAllByQuery(query, userId.toString(), "${OrderContract.DATE} DESC")

        mapAllProducts(orders)

        return orders
    }

    /**
     * @inheritDoc
     */
    override fun getAllOrders(): List<Order> {
        val orders = getAllByQuery(null, null, "${OrderContract.DATE} DESC")

        mapAllProducts(orders)

        return orders
    }

    /**
     * @inheritDoc
     */
    @SuppressLint("Range")
    override fun getById(id: Int): Order? {
        val query = "${OrderContract.ID} = ?"
        val orders = getAllByQuery(query, id.toString())
        val order = orders.firstOrNull() ?: return null

        mapAllProducts(listOf(order))

        return order
    }

    private fun mapAllProducts(orders: List<Order>) {
        for (order in orders) {
            val products = getProductsForOrder(order.orderId)
            order.products.clear()
            order.products.addAll(products)
        }
    }

    private fun getProductsForOrder(orderId: Int): MutableList<Product> {
        val products = mutableListOf<Product>()

        val selectQuery =
            "SELECT ${ProductContract.TABLE_NAME}.* FROM ${OrderContract.TABLE_NAME} " +
                    "JOIN ${OrderDetailsContract.TABLE_NAME} ON ${OrderContract.TABLE_NAME}.${OrderContract.ID} = ${OrderDetailsContract.TABLE_NAME}.${OrderDetailsContract.ORDER_ID} " +
                    "JOIN ${ProductContract.TABLE_NAME} ON ${OrderDetailsContract.TABLE_NAME}.${OrderDetailsContract.PROD_ID} = ${ProductContract.TABLE_NAME}.${ProductContract.ID} " +
                    "WHERE ${OrderContract.TABLE_NAME}.${OrderContract.ID} = ?"

        val db = helper.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(orderId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val product = cursor.toProduct()
                products.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return products
    }

    private fun saveProductsToOrder(products: List<Product>, orderId: Int) {
        for (product in products) {
            val orderDetails = OrderDetails(
                -1,
                orderId,
                product.productId
            )

            orderDetailsRepository.save(orderDetails)
        }
    }
}