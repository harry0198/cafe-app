package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.OrderDetails
import me.harrydrummond.cafeapplication.data.repository.toOrderDetails

object OrderDetailsContract : BaseContract<OrderDetails> {
    const val ORDER_ID = "DetailsOrderId"
    const val PROD_ID = "DetailsProdId"

    override val TABLE_NAME = "OrderDetails"
    override val ID = "OrderDetailsId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $ORDER_ID INTEGER NOT NULL, " +
            "$PROD_ID INTEGER NOT NULL )"

    /**
     * @inheritDoc
     */
    override fun toEntity(cursor: Cursor): OrderDetails = cursor.toOrderDetails()

    /**
     * @inheritDoc
     */
    override fun getId(entity: OrderDetails): Int = entity.orderDetailsId

    /**
     * @inheritDoc
     */
    override fun getEntityValues(entity: OrderDetails, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.orderDetailsId)
        cv.put(ORDER_ID, entity.orderId)
        cv.put(PROD_ID, entity.productId)

        return cv
    }
}