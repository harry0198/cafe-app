package me.harrydrummond.cafeapplication.ui.customer.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Payment
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IPaymentRepository
import me.harrydrummond.cafeapplication.logic.mapDuplicatesToQuantity
import java.time.Instant
import java.util.Date
import javax.inject.Inject

/**
 * CartViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to login.
 *
 * @see CartFragment
 * @author Harry Drummond
 */
@HiltViewModel
class CartViewModel @Inject constructor(private val orderRepository: IOrderRepository, private val paymentRepository: IPaymentRepository): ViewModel() {

    private val _uiState: MutableLiveData<CartFragmentUIState> = MutableLiveData(CartFragmentUIState())
    val uiState: LiveData<CartFragmentUIState> get() = _uiState

    /**
     * Refreshes the cart product items.
     * Retrieves cart from the user repository.
     *
     * @see Cart
     */
    fun refreshCart() {
        _uiState.value = _uiState.value?.copy(loading = true)

        // Refresh in background
        viewModelScope.launch {
            val products = Cart.getInstance().getProducts()
            val mappedProducts = products.mapDuplicatesToQuantity()
            println("Mapped prods" + mappedProducts.size)
            println("actual prods" + products.size)
            _uiState.postValue(
                _uiState.value?.copy(
                    loading = false,
                    cartProducts = mappedProducts
                )
            )
        }
    }

    /**
     * Updates the quantity of a product within a cart
     *
     * @param productId Id of product to update
     * @param quantity New quantity of product in cart
     *
     * @see Cart
     */
    fun updateQuantity(product: Product, quantity: Int) {

        Cart.getInstance().clearProductsMatching(product)
        Cart.getInstance().addProduct(product, quantity)

        refreshCart()
    }

    /**
     * Returns the total cost of the current cart.
     */
    fun getTotalCost(): Double {
        return Cart.getInstance().getProducts().sumOf { it.productPrice }
    }

    /**
     * Is the current cart empty
     */
    fun isCartEmpty(): Boolean {
        return Cart.getInstance().getProducts().isEmpty()
    }

    /**
     * Places an order and updates the UIState
     */
    fun placeOrder(cardNo: String, expiry: String, cvv: String) {
        _uiState.value = _uiState.value?.copy(loading = true)

        // Place order in background
        viewModelScope.launch {
            val cartProducts: MutableList<Product> = mutableListOf()
            cartProducts.addAll(Cart.getInstance().getProducts())

            if (cartProducts.isEmpty()) {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.CartEmpty))
                return@launch
            }

            val order = Order(
                products = cartProducts,
                status = Status.NONE,
                userId = AuthenticatedUser.getInstance().getUserId(),
                orderId = -1,
                timestamp = Date.from(Instant.now())
            )

            val saved = orderRepository.save(order)
            val payment = Payment(-1, saved, "CARD", order.products.sumOf { it.productPrice }, Date.from(Instant.now()))
            paymentRepository.save(payment)

            if (saved != -1) {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.OrderPlaced)
                clearCart()
            } else {
                _uiState.value =
                    _uiState.value?.copy(
                        loading = false,
                        errorMessage = "Unable to place order"
                    )
            }
        }
    }

    /**
     * Indicates that the UI has shown the error message and can be removed from state
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Indicates that the event ha been handled by the UI
     */
    fun eventHandled() {
        _uiState.value = _uiState.value?.copy(event = null)
    }

    private fun clearCart() {
        // Save user cart as empty and refresh
        viewModelScope.launch {
            Cart.getInstance().clear()
        }.invokeOnCompletion {
            refreshCart()
        }
    }
}

/**
 * UIState for the cart fragment.
 */
data class CartFragmentUIState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val cartProducts: List<Pair<Int, Product>> = emptyList(),
    val event: Event? = null
)

/**
 * Event definition for the product view model.
 */
sealed interface Event {
    data object CartEmpty: Event
    data object OrderPlaced: Event
}