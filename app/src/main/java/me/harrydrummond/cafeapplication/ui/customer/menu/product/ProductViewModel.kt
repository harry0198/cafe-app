package me.harrydrummond.cafeapplication.ui.customer.menu.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.model.UserReview
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.logic.Counter
import javax.inject.Inject

class ProductViewModel: ViewModel() {

    private val counter = Counter(1, 1, 15)
    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val _uiState: MutableLiveData<ProductViewUIState> = MutableLiveData(ProductViewUIState())
    val uiState: LiveData<ProductViewUIState> get() = _uiState
    lateinit var product: Product

    /**
     * This view requires a productmodel to display correctly.
     * This should be called first by the view(s)
     */
    fun initialize(productModel: Product) {
        this.product = productModel
    }

    /**
     * Adds the current item to the cart by the quantity selected.
     */
    fun addToCart() {
        _uiState.value = _uiState.value?.copy(loading = true)
        userRepository.partialLoadUserCart().addOnCompleteListener { cartTask ->
            val cart = cartTask.result
            if (cartTask.isSuccessful && cart != null) {
                    cart.addToCart(product.productId, counter.getValue())
                    userRepository.saveUserCart(cart).addOnCompleteListener { saveTask ->
                        if (saveTask.isSuccessful) {
                            _uiState.value = _uiState.value?.copy(loading = false, event = Event.ItemAddedToCart)
                        } else {
                            _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Could not save to cart")
                        }
                    }

            } else {
                _uiState.value =
                    _uiState.value?.copy(loading = false, errorMessage = "Could not fetch cart")
            }
        }
    }

    /**
     * Increments the number of products to add to the cart.
     */
    fun incrementQuantity() {
        counter.increment(1)
        _uiState.value = _uiState.value?.copy(quantity = counter.getValue())
    }

    /**
     * Decrements the number of products to add to the cart.
     */
    fun decrementQuantity() {
        counter.decrement(1)
        _uiState.value = _uiState.value?.copy(quantity = counter.getValue())
    }

    /**
     * Prompts that the error message has been shown and should update the uistate.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Indicates that the event has been processed by the UI and UIState should update.
     */
    fun eventExecuted() {
        _uiState.value = _uiState.value?.copy(event = null)
    }
}

/**
 * Data class for the ProductView UI State.
 * Includes fields for loading, errors and events.
 */
data class ProductViewUIState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null,
    val quantity: Int = 1,
)

/**
 * Event definition for the product view model.
 */
sealed interface Event {
    data object ItemAddedToCart: Event
}