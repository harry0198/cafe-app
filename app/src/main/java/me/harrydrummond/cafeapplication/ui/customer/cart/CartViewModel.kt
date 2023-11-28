package me.harrydrummond.cafeapplication.ui.customer.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.Cart
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.ProductQuantity
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository

/**
 * CartViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to login.
 *
 * @see CartFragment
 * @author Harry Drummond
 */
class CartViewModel : ViewModel() {

    private val productRepository: IProductRepository = FirestoreProductRepository()
    private val orderRepository: IOrderRepository = FirestoreOrderRepository(productRepository)
    private val userRepository: IUserRepository = FirestoreUserRepository(productRepository)
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

        userRepository.partialLoadUserCart().addOnCompleteListener { cartTask ->
            val cart = cartTask.result
            if (cartTask.isSuccessful && cart != null) {
                    userRepository.fullLoadUserCart(cart).addOnCompleteListener { listTask ->
                        if (listTask.isSuccessful) {
                            _uiState.value = _uiState.value?.copy(loading = false, cartProducts = listTask.result ?: emptyList())
                        } else {
                            _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Failed to load cart fully")
                        }
                    }
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Failed to load cart partially")
            }
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
    fun updateQuantity(productId: String, quantity: Int) {
        userRepository.partialLoadUserCart().addOnCompleteListener { cartTask ->
            val cart = cartTask.result
            if (cartTask.isSuccessful && cart != null) {
                cart.updateCartItem(productId, quantity)
                userRepository.saveUserCart(cart).addOnCompleteListener { refreshCart() }
            }
        }
    }

    /**
     * Places an order and updates the UIState
     */
    fun placeOrder() {
        _uiState.value = _uiState.value?.copy(loading = true)

        val mappedProductQuantity = _uiState.value?.cartProducts?.map { item ->
            ProductQuantity(item.second.productId, item.first)
        }
        val userId = Firebase.auth.currentUser?.uid

        if (mappedProductQuantity.isNullOrEmpty() || userId == null) {
            return
        }

        val order = Order(
            products = mappedProductQuantity,
            status = Status.NONE,
            userId = userId
        )

        orderRepository.saveOrder(order).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.OrderPlaced)
                clearCart()
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Unable to place order")
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
        userRepository.saveUserCart(Cart(mutableListOf())).addOnSuccessListener {
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
    val cartProducts: List<Pair<Int, ProductModel>> = emptyList(),
    val event: Event? = null
)

/**
 * Event definition for the product view model.
 */
sealed interface Event {
    data object OrderPlaced: Event
}