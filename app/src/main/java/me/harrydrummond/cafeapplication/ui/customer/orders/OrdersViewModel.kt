package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import javax.inject.Inject

/**
 * OrdersViewModel class which provides the business logic to the view class
 * using the MVVM pattern.
 *
 * @see OrdersFragment
 * @author Harry Drummond
 */
@HiltViewModel
class OrdersViewModel @Inject constructor(private val orderRepository: IOrderRepository) : ViewModel() {

    /**
     * Marks an order as collected IF already in ready state.
     *
     * @param order Order to mark as collected
     */
    fun tryCollectOrder(order: Order): Order {
        var newOrder = order
        if (order.status == Status.READY) {
            newOrder = order.copy(status = Status.COLLECTED)
            orderRepository.update(newOrder)
        }

        return newOrder
    }
}