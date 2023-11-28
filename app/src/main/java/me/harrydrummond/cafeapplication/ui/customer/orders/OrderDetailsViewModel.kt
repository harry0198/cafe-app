package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository

/**
 * OrderDetailsViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see OrderDetailsActivity
 * @author Harry Drummond
 */
class OrderDetailsViewModel: ViewModel() {

    private val productRepository: IProductRepository = FirestoreProductRepository()
    private val orderRepository: IOrderRepository = FirestoreOrderRepository(productRepository)
    private val _uiState: MutableLiveData<OrderDetailsUIState> = MutableLiveData(OrderDetailsUIState())
    val uiState: LiveData<OrderDetailsUIState> get() = _uiState

    /**
     * This view depends on an order id to show appropriate information.
     * This function should be called to provide the viewmodel with an order id to set the UIState
     * with and perform operations with.
     *
     * @param orderId Id in the database of the order
     */
    fun initialize(orderId: String) {
        _uiState.value = _uiState.value?.copy(loading = true, orderId = orderId)

        // Update the orders in the ui state
        orderRepository.getOrder(orderId).continueWith {
            val order = it.result
            if (it.isSuccessful && order != null) {
                orderRepository.fullLoadOrderProducts(order).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = _uiState.value?.copy(
                            loading = false,
                            productsAndQuantity = task.result ?: emptyList()
                        )
                    } else {
                        _uiState.value = _uiState.value?.copy(
                            loading = false,
                            errorMessage = "Unable to load order details"
                        )
                    }
                }
            }
        }
    }

    /**
     * Inidcates that the view has shown the error message
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * Data class for the OrderDetails UI State.
 * Includes fields for loading, errors and the products and quantity in an order.
 */
data class OrderDetailsUIState(
    val orderId: String? = "TBC",
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val productsAndQuantity: List<Pair<Int, ProductModel>> = listOf(),
)