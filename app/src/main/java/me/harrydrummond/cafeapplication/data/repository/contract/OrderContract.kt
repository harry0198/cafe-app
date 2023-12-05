package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.toOrder
import me.harrydrummond.cafeapplication.data.repository.toProduct

object OrderContract : BaseContract<Order> {
    const val CUSTOMER_ID = "CusId"
    const val DATE = "OrderDate"
    const val STATUS = "OrderStatus"

    override val TABLE_NAME = "Order"
    override val ID = "OrderId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $CUSTOMER_ID INTEGER, " +
            "$DATE NUMERIC, $STATUS TEXT )"

    /**
     * @inheritDoc
     */
    override fun toEntity(cursor: Cursor): Order = cursor.toOrder()

    /**
     * @inheritDoc
     */
    override fun getId(entity: Order): Int = entity.orderId

    /**
     * @inheritDoc
     */
    override fun getEntityValues(entity: Order): ContentValues {
        val cv = ContentValues()

        cv.put(CUSTOMER_ID, entity.userId)
        cv.put(DATE, entity.timestamp.toInstant().epochSecond)
        cv.put(STATUS, entity.status.toString())

        return cv
    }
}