package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.OrderDetails
import me.harrydrummond.cafeapplication.data.repository.contract.OrderDetailsContract

class SQLiteOrderDetailsRepository(private val helper: DataBaseHelper): AbstractSQLiteRepository<OrderDetails>(helper, OrderDetailsContract) {

    fun getProductsByOrderId(orderId: Int) {

    }

    fun deleteByOrderId(orderId: Int): Boolean {
        val db = helper.writableDatabase
        val result = db.delete(OrderDetailsContract.TABLE_NAME, "${OrderDetailsContract.ORDER_ID} = $orderId", null) == 1

        db.close()
        return result
    }
}