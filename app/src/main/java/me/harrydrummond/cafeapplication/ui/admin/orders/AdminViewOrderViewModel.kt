package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.model.Notification
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.INotificationRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.logic.NotificationHelper
import me.harrydrummond.cafeapplication.logic.mapDuplicatesToQuantity
import javax.inject.Inject

/**
 * AdminViewOrderViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see AdminViewOrderActivity
 * @author Harry Drummond
 */
@HiltViewModel
class AdminViewOrderViewModel @Inject constructor(private val orderRepository: IOrderRepository,
                                                  private val notificationHelper: NotificationHelper,
                                                  private val notificationRepository: INotificationRepository): ViewModel() {

    private lateinit var order: Order
    private val _uiState: MutableLiveData<OrderUiState> = MutableLiveData(OrderUiState())
    val orderUiState: LiveData<OrderUiState> get() = _uiState

    /**
     * This view depends on an order id to show appropriate information.
     * This function should be called to provide the viewmodel with an order id to set the UIState
     * with and perform operations with.
     *
     * @param orderId Id in the database of the order
     */
    fun initialize(order: Order) {
        this.order = order
        _uiState.postValue(_uiState.value?.copy(productData = order.products.mapDuplicatesToQuantity()))
    }

    /**
     * Function for notifying the viewmodel that the error message has been shown and can be
     * removed from state.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Function to change the status of an order.
     *
     * @param status Status to change the order to.
     *
     * @see Status
     */
    fun changeOrderStatus(status: Status) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        // Save order status in background
        viewModelScope.launch {
            val saved = orderRepository.update(order.copy(status = status))
            if (saved) {
                val notification = Notification(-1, order.userId, "Order ${order.orderId} status updated!")
                notificationRepository.save(notification)
                notificationHelper.showOrderStatusNotification(status)
                _uiState.postValue(_uiState.value?.copy(isLoading = false, orderStatus = status))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        isLoading = false,
                        errorMessage = "Failed to Update Status"
                    )
                )
            }
        }
    }
}

/**
 * UI State class, contains variables to update the UI.
 */
data class OrderUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val productData: List<Pair<Int, Product>> = emptyList(),
    val orderStatus: Status = Status.RECEIVED
)