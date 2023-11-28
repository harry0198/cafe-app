package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.OrderRepository

data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val productData: List<Pair<Int, ProductModel>> = emptyList(),
    val orderStatus: Status = Status.NONE
)

class AdminViewOrderViewModel: ViewModel() {

    private var order: Order? = null
    private val orderRepository = OrderRepository()
    private val _uiState: MutableLiveData<OrderUiState> = MutableLiveData(OrderUiState())
    val orderUiState: LiveData<OrderUiState> get() = _uiState

    fun initialize(orderId: String) {
        orderRepository.getOrder(orderId).continueWith {
            val order = it.result
            this.order = order
            if (it.isSuccessful && order != null) {
                orderRepository.fullLoadOrderProducts(order) { orderProducts ->
                    _uiState.value = _uiState.value?.copy(productData = orderProducts)
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    fun changeOrderStatus(status: Status) {
        order?.let {
            it.status = status
            orderRepository.saveOrder(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value?.copy(isLoading = false, orderStatus = status)
                } else {
                    _uiState.value = _uiState.value?.copy(isLoading = false, errorMessage = "Failed to Update Status")
                }
            }
        }
    }
}