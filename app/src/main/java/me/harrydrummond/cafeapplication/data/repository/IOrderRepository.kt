package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product

/**
 * Interface defining signatures to perform CRUD operations on an order repository.
 *
 * @see Order
 */
interface IOrderRepository: CrudRepository<Order> {

}