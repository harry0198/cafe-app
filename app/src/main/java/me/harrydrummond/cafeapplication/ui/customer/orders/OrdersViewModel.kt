package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.OrderRepository
import me.harrydrummond.cafeapplication.data.repository.ProductRepository

class OrdersViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    val orders = MutableLiveData<List<Order>>(listOf())


    fun refreshOrders() {
        orderRepository.getOrdersByUser().continueWith { task ->
            if (task.isSuccessful) {
                orders.value = task.result
            }
        }
    }
}