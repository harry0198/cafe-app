package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.OrderRepository

class AdminOrdersViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    val orders = MutableLiveData<List<Order>>(listOf())

    fun refreshOrders() {
        orderRepository.getOrders().continueWith { task ->
            if (task.isSuccessful) {
                orders.value = task.result
            }
        }
    }
}