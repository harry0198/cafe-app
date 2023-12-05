package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import javax.inject.Inject

/**
 * AdminOrdersViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see AdminViewOrderActivity
 * @author Harry Drummond
 */
@HiltViewModel
class AdminOrdersViewModel @Inject constructor(private val orderRepository: IOrderRepository): ViewModel() {

    private val _uiState: MutableLiveData<AdminOrdersUIState> = MutableLiveData(AdminOrdersUIState())
    val uiState: LiveData<AdminOrdersUIState> get() = _uiState

    /**
     * Refreshes the orders and fetches them from the database.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        viewModelScope.launch {
            val orders = orderRepository.getAllOrders()
            _uiState.value = _uiState.value?.copy(loading = false, orders = orders)
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