package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Payment
import me.harrydrummond.cafeapplication.data.repository.toPayment

/**
 * Contains the table name and columns for creating the sqlite database.
 * Also contains the create statement.
 */
object PaymentContract : BaseContract<Payment> {
    const val ORDER_ID = "OrderId"
    const val PAYMENT_TYPE = "PaymentType"
    const val AMOUNT = "Amount"
    const val PAYMENT_DATE = "PaymentDate"

    override val TABLE_NAME = "Payment"
    override val ID = "PaymentId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $ORDER_ID INTEGER NOT NULL, " +
            "$PAYMENT_TYPE TEXT NOT NULL, $AMOUNT NUMERIC NOT NULL, $PAYMENT_DATE NUMERIC NOT NULL )"

    /**
     * @inheritDoc
     */
    override fun toEntity(cursor: Cursor): Payment = cursor.toPayment()

    /**
     * @inheritDoc
     */
    override fun getId(entity: Payment): Int = entity.paymentId

    /**
     * @inheritDoc
     */
    override fun getEntityValues(entity: Payment, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.paymentId)
        cv.put(ORDER_ID, entity.orderId)
        cv.put(AMOUNT, entity.amount)
        cv.put(PAYMENT_TYPE, entity.paymentType)
        cv.put(PAYMENT_DATE, entity.paymentDate.toInstant().epochSecond)

        return cv
    }
}