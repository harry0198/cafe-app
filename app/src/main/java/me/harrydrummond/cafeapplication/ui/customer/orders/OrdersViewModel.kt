package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

/**
 * OrdersViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see OrdersFragment
 * @author Harry Drummond
 */
class OrdersViewModel : ViewModel() {

    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val productRepository: IProductRepository = FirestoreProductRepository(userRepository)
    private val orderRepository: IOrderRepository = FirestoreOrderRepository(productRepository)
    private val _uiState: MutableLiveData<OrdersUIState> = MutableLiveData(OrdersUIState())
    val uiState: LiveData<OrdersUIState> get() = _uiState

    /**
     * Fetches orders from the repository and then updates the ui state whenever the async process
     * returns a value.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        orderRepository.getOrdersByUser().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.value = _uiState.value?.copy(loading = false, orders = task.result)
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Failed to update orders")
            }
        }
    }

    /**
     * Function for notifying the viewmodel that the error message has been shown and can be
     * removed from state.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * UI State class, contains variables to update the UI.
 */
data class OrdersUIState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val orders: List<Order> = emptyList(),
)