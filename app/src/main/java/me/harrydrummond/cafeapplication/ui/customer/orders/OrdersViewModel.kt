package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
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
@HiltViewModel
class OrdersViewModel @Inject constructor(private val orderRepository: IOrderRepository) : ViewModel() {

    private val _uiState: MutableLiveData<OrdersUIState> = MutableLiveData(OrdersUIState())
    val uiState: LiveData<OrdersUIState> get() = _uiState

    /**
     * Fetches orders from the repository and then updates the ui state whenever the async process
     * returns a value.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        val orders = orderRepository.getOrdersByUserId(AuthenticatedUser.getInstance().getUserId())
        _uiState.postValue(_uiState.value?.copy(loading = false, orders = orders))
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