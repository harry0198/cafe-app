package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository

/**
 * AdminOrdersViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see AdminViewOrderActivity
 * @author Harry Drummond
 */
class AdminOrdersViewModel : ViewModel() {

    private val _uiState: MutableLiveData<AdminOrdersUIState> = MutableLiveData(AdminOrdersUIState())
    val uiState: LiveData<AdminOrdersUIState> get() = _uiState

    /**
     * Refreshes the orders and fetches them from the database.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        orderRepository.getOrders().addOnCompleteListener { task ->
            val orders = task.result
            if (task.isSuccessful && orders != null) {
                _uiState.value = _uiState.value?.copy(loading = false, orders = orders)
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Could not fetch orders")
            }
        }
    }

    /**
     * Indicates that the view has shown the error message and the view can be updated
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * Data class for defining the AdminOrder UI State
 */
data class AdminOrdersUIState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val orders: List<Order> = emptyList(),
)