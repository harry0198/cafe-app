package me.harrydrummond.cafeapplication.data.repository

import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Status
import java.time.Instant
import java.util.Date


/**
 * Maps a cursor to a customer
 *
 * @return Customer from the cursor.
 */
fun Cursor.toCustomer(): Customer {
    return Customer(
        this.getInt(0),
        this.getString(1),
        this.getString(2),
        this.getString(3),
        this.getString(4),
        this.getString(5),
        this.getInt(6) == 1
    )
}

/**
 * Maps a cursor to an employee
 *
 * @return Employee from the cursor.
 */
fun Cursor.toEmployee(): Employee {
    return Employee(
        this.getInt(0),
        this.getString(1),
        this.getString(2),
        this.getString(3),
        this.getString(4),
        this.getString(5),
        this.getInt(6) == 1
    )
}

/**
 * Maps a cursor to an order
 *
 * @return Order from the cursor.
 */
fun Cursor.toOrder(): Order {
    return Order(
        this.getInt(0),
        Date.from(Instant.ofEpochSecond(this.getLong(2))),
        Status.valueOf(this.getString(3)),
        this.getInt(1)
    )
}

fun Cursor.toProduct(): Product {
    return Product(
        this.getInt(0),
        this.getString(1),
        this.getDouble(2),
        this.getString(3), //TODO BLOB
        this.getString(5),
        this.getInt(4) == 1

    )
}
