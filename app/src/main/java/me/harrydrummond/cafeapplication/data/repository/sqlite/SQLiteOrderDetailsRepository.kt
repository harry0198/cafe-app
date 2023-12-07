package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.OrderDetails
import me.harrydrummond.cafeapplication.data.repository.contract.OrderDetailsContract

/**
 * An sqlite repository for performing operations on the order details join database.
 *
 * @param helper [DataBaseHelper] providing the sqlite database.
 */
class SQLiteOrderDetailsRepository(private val helper: DataBaseHelper): AbstractSQLiteRepository<OrderDetails>(helper, OrderDetailsContract) {

    /**
     * Deletes orders by their ID.
     *
     * @param orderId Id of the order to delete.
     *
     * @return If the order was deleted successfully or not.
     */
    fun deleteByOrderId(orderId: Int): Boolean {
        val db = helper.writableDatabase
        val result = db.delete(OrderDetailsContract.TABLE_NAME, "${OrderDetailsContract.ORDER_ID} = ?", arrayOf( orderId.toString())) != -1

        db.close()
        return result
    }
}