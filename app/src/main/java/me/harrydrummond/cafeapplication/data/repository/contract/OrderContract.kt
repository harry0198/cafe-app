package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.toOrder
import me.harrydrummond.cafeapplication.data.repository.toProduct

object OrderContract : BaseContract<Order> {
    const val CUSTOMER_ID = "OrderCusId"
    const val DATE = "OrderDate"
    const val STATUS = "OrderStatus"

    override val TABLE_NAME = "CafeOrder"
    override val ID = "OrderId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $CUSTOMER_ID INTEGER NOT NULL, " +
            "$DATE NUMERIC NOT NULL, $STATUS TEXT )"

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
    override fun getEntityValues(entity: Order, withPrimaryKey: Boolean, hashing: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.orderId)
        cv.put(CUSTOMER_ID, entity.userId)
        cv.put(DATE, entity.timestamp.toInstant().epochSecond)
        cv.put(STATUS, entity.status.toString())

        return cv
    }
}