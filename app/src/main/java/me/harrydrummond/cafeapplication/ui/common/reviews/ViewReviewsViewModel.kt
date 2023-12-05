package me.harrydrummond.cafeapplication.ui.common.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository

class ViewReviewsViewModel: ViewModel() {

    private lateinit var product: Product
    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val productRepository: IProductRepository = FirestoreProductRepository(userRepository)
    private val _uiState: MutableLiveData<ViewReviewsUIState> = MutableLiveData(ViewReviewsUIState())
    val uiState: LiveData<ViewReviewsUIState> get() = _uiState

    /**
     * This view requires a productmodel to display correctly.
     * This should be called first by the view(s)
     */
    fun initialize(productModel: Product) {
        this.product = productModel
        updateReviews()
    }

    /**
     * Saves a review to the database
     *
     * @param review Review to save
     */
    fun saveReview(review: String) {
        _uiState.value = _uiState.value?.copy(loading = true)
        val userId = userRepository.getLoggedInUserId()
        if (userId != null){
            productRepository.saveReview(product.productId, Review(userId, review))
            _uiState.value = _uiState.value?.copy(loading = false, event = Event.ReviewSaved)
            updateReviews()
        }
    }

    /**
     * Updates the reviews from the database
     */
    private fun updateReviews() {
        _uiState.value = _uiState.value?.copy(loading = true)

        productRepository.getUserReviewsForProduct(productId = product.productId) {
            if (it != null) {
                _uiState.value = _uiState.value?.copy(reviews = it, loading = false)
            } else {
                _uiState.value =
                    _uiState.value?.copy(loading = false, errorMessage = "Could not update review list")
            }
        }

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

    /**
     * Data class for the ProductView UI State.
     * Includes fields for loading, errors and events.
     */
    data class ViewReviewsUIState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val event: Event? = null,
        val reviews: List<UserReview> = emptyList()
    )

    /**
     * Event definition for the product view model.
     */
    sealed interface Event {
        data object ReviewSaved: Event
    }
}