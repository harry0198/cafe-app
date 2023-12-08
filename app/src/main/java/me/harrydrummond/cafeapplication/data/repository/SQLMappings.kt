package me.harrydrummond.cafeapplication.data.repository

import android.annotation.SuppressLint
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.OrderDetails
import me.harrydrummond.cafeapplication.data.model.Payment
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.contract.CustomerContract
import me.harrydrummond.cafeapplication.data.repository.contract.EmployeeContract
import me.harrydrummond.cafeapplication.data.repository.contract.OrderContract
import me.harrydrummond.cafeapplication.data.repository.contract.OrderDetailsContract
import me.harrydrummond.cafeapplication.data.repository.contract.PaymentContract
import me.harrydrummond.cafeapplication.data.repository.contract.ProductContract
import me.harrydrummond.cafeapplication.data.repository.contract.ReviewContract
import me.harrydrummond.cafeapplication.logic.toBitmap
import java.time.Instant
import java.util.Date

/**
 * Maps a cursor to a Payment
 *
 * @return Payment from the cursor.
 */
@SuppressLint("Range")
fun Cursor.toPayment(): Payment {
    return Payment(
        this.getInt(this.getColumnIndex(PaymentContract.ID)),
        this.getInt(this.getColumnIndex(PaymentContract.ORDER_ID)),
        this.getString(this.getColumnIndex(PaymentContract.PAYMENT_TYPE)),
        this.getDouble(this.getColumnIndex(PaymentContract.AMOUNT)),
        Date.from(Instant.ofEpochSecond(this.getLong(this.getColumnIndex(PaymentContract.PAYMENT_DATE))))
    )
}

/**
 * Maps a cursor to a customer
 *
 * @return Customer from the cursor.
 */
@SuppressLint("Range")
fun Cursor.toCustomer(): Customer {
    return Customer(
        this.getInt(this.getColumnIndex(CustomerContract.ID)),
        this.getString(this.getColumnIndex(CustomerContract.FULL_NAME)),
        this.getString(this.getColumnIndex(CustomerContract.EMAIL)),
        this.getString(this.getColumnIndex(CustomerContract.PHONE_NO)),
        this.getString(this.getColumnIndex(CustomerContract.USERNAME)),
        this.getString(this.getColumnIndex(CustomerContract.PASSWORD)),
        this.getInt(this.getColumnIndex(CustomerContract.IS_ACTIVE)) == 1
    )
}

/**
 * Maps a cursor to an employee
 *
 * @return Employee from the cursor.
 */
@SuppressLint("Range")
fun Cursor.toEmployee(): Employee {
    return Employee(
        this.getInt(this.getColumnIndex(EmployeeContract.ID)),
        this.getString(this.getColumnIndex(EmployeeContract.FULL_NAME)),
        this.getString(this.getColumnIndex(EmployeeContract.EMAIL)),
        this.getString(this.getColumnIndex(EmployeeContract.PHONE_NO)),
        this.getString(this.getColumnIndex(EmployeeContract.USERNAME)),
        this.getString(this.getColumnIndex(EmployeeContract.PASSWORD)),
        this.getInt(this.getColumnIndex(EmployeeContract.IS_ACTIVE)) == 1
    )
}

/**
 * Maps a cursor to an order
 *
 * @return Order from the cursor.
 */
@SuppressLint("Range")
fun Cursor.toOrder(): Order {
    return Order(
        this.getInt(this.getColumnIndex(OrderContract.ID)),
        Date.from(Instant.ofEpochSecond(this.getLong(this.getColumnIndex(OrderContract.DATE)))),
        Status.valueOf(this.getString(this.getColumnIndex(OrderContract.STATUS))),
        this.getInt(this.getColumnIndex(OrderContract.CUSTOMER_ID)),
        mutableListOf()
    )
}

/**
 * Maps a cursor to order details
 *
 * @return OrderDetails from cursor
 */
@SuppressLint("Range")
fun Cursor.toOrderDetails(): OrderDetails {
    return OrderDetails(
        this.getInt(this.getColumnIndex(OrderDetailsContract.ID)),
        this.getInt(this.getColumnIndex(OrderDetailsContract.ORDER_ID)),
        this.getInt(this.getColumnIndex(OrderDetailsContract.PROD_ID))
    )
}

@SuppressLint("Range")
fun Cursor.toReview(): Review {
    return Review(
        this.getInt(this.getColumnIndex(ReviewContract.ID)),
        this.getInt(this.getColumnIndex(ReviewContract.USER_ID)),
        this.getInt(this.getColumnIndex(ReviewContract.PRODUCT_ID)),
        this.getString(this.getColumnIndex(ReviewContract.REVIEW)),
        this.getFloat(this.getColumnIndex(ReviewContract.RATING))
    )
}

@SuppressLint("Range")
fun Cursor.toProduct(): Product {
    return Product(
        this.getInt(this.getColumnIndex(ProductContract.ID)),
        this.getString(this.getColumnIndex(ProductContract.NAME)),
        this.getDouble(this.getColumnIndex(ProductContract.PRICE)),
        this.getBlob(this.getColumnIndex(ProductContract.IMAGE)),
        this.getString(this.getColumnIndex(ProductContract.DESCRIPTION)),
        this.getInt(this.getColumnIndex(ProductContract.AVAILABLE)) == 1

    )
}
