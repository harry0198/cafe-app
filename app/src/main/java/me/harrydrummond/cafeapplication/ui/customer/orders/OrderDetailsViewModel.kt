package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.logic.mapDuplicatesToQuantity
import java.util.Date

/**
 * OrderDetailsViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see OrderDetailsActivity
 * @author Harry Drummond
 */
class OrderDetailsViewModel: ViewModel() {

    private val _uiState: MutableLiveData<OrderDetailsUIState> = MutableLiveData(OrderDetailsUIState())
    private lateinit var order: Order
    val uiState: LiveData<OrderDetailsUIState> get() = _uiState

    /**
     * This view depends on an order id to show appropriate information.
     * This function should be called to provide the viewmodel with an order id to set the UIState
     * with and perform operations with.
     *
     * @param orderId Id in the database of the order
     */
    fun initialize(order: Order) {
        this.order = order
        _uiState.postValue(_uiState.value?.copy(productsAndQuantity = order.products.mapDuplicatesToQuantity(), dateOfOrder = order.timestamp))
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
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val dateOfOrder: Date? = null,
    val productsAndQuantity: List<Pair<Int, Product>> = listOf(),
)