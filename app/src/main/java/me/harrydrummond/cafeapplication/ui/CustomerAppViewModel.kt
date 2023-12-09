package me.harrydrummond.cafeapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.harrydrummond.cafeapplication.data.model.Notification
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.INotificationRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.logic.NotificationHelper
import javax.inject.Inject

/**
 * Customer app viewmodel containing the business logic for the customer app shared properties.
 *
 * @see AppActivity
 * @see CustomerAppUIState
 */
@HiltViewModel
class CustomerAppViewModel @Inject constructor(private val orderRepository: IOrderRepository, private val notificationRepository: INotificationRepository, private val notificationHelper: NotificationHelper): ViewModel() {

    private var _orders: MutableLiveData<List<Order>> = MutableLiveData(emptyList())
    private var _uiState: MutableLiveData<CustomerAppUIState> = MutableLiveData(CustomerAppUIState())
    val orders: LiveData<List<Order>> get() = _orders
    val uiState: LiveData<CustomerAppUIState> get() = _uiState

    /**
     * Fetches orders from the repository and then updates the ui state whenever the async process
     * returns a value.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        val orders = orderRepository.getOrdersByUserId(AuthenticatedUser.getInstance().getUserId())
        _uiState.postValue(_uiState.value?.copy(loading = false))
        _orders.postValue(orders)
    }

    /**
     * Show notifications to the user.
     */
    fun showNotifications() {
        val notifications = notificationRepository.getNotificationsForUser(AuthenticatedUser.getInstance().getUserId())

        for (notification in notifications) {
            notificationHelper.sendNotif("Notification", notification.notificationMessage)
            notificationRepository.delete(notification)
        }
    }


    /**
     * Data class for holding the ui state of the AppActivity.
     */
    data class CustomerAppUIState(
        val loading: Boolean = false
    )
}