package me.harrydrummond.cafeapplication.ui.common.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.OrderRepository

class OrderDetailsViewModel: ViewModel() {

    private lateinit var order: String
    val products = MutableLiveData<List<Pair<Int, ProductModel>>>(listOf())
    val orderRepository = OrderRepository()

    fun initialize(orderId: String) {
        order = orderId

        orderRepository.getOrder(orderId).continueWith {
            val order = it.result
            if (it.isSuccessful && order != null) {
                orderRepository.fullLoadOrderProducts(order) { orderProducts ->
                    products.value = orderProducts
                }
            }
        }
    }
}