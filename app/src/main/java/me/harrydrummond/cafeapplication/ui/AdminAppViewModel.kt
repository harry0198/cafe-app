package me.harrydrummond.cafeapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import javax.inject.Inject

/**
 * View model for the administrator application containing business logic.
 *
 * @see AdminAppActivity
 * @see AdminAppUIState
 */
@HiltViewModel
class AdminAppViewModel @Inject constructor(private val orderRepository: IOrderRepository): ViewModel() {

    private var _orders: MutableLiveData<List<Order>> = MutableLiveData(emptyList())
    private var _uiState: MutableLiveData<AdminAppUIState> = MutableLiveData(AdminAppUIState())
    val orders: LiveData<List<Order>> get() = _orders
    val uiState: LiveData<AdminAppUIState> get() = _uiState

    /**
     * Fetches orders from the repository and then updates the ui state whenever the async process
     * returns a value.
     */
    fun refreshOrders() {
        _uiState.value = _uiState.value?.copy(loading = true)

        val orders = orderRepository.getAllOrders()
        _uiState.postValue(_uiState.value?.copy(loading = false))
        _orders.postValue(orders)
    }

    /**
     * Data class for holding the ui state of the AppActivity.
     */
    data class AdminAppUIState(
        val loading: Boolean = false
    )
}