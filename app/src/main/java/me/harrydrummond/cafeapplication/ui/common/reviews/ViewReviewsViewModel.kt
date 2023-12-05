package me.harrydrummond.cafeapplication.ui.common.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.AuthenticatedUser
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IReviewRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

@HiltViewModel
class ViewReviewsViewModel @Inject constructor(private val reviewRepository: IReviewRepository): ViewModel() {

    private lateinit var product: Product
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
     * @param reviewStr Review to save
     */
    fun saveReview(reviewStr: String) {
        _uiState.value = _uiState.value?.copy(loading = true)

        val review = Review(
            -1,
            AuthenticatedUser.getInstance().getUserId(),
            product.productId,
            reviewStr
        )

        viewModelScope.launch {
            val saved = reviewRepository.save(review)
            if (saved != -1) {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.ReviewSaved)
                updateReviews()
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        loading = false,
                        errorMessage = "Failed to save review."
                    )
                )
            }
        }
    }

    /**
     * Updates the reviews from the database
     */
    private fun updateReviews() {
        _uiState.value = _uiState.value?.copy(loading = true)

        val reviews = reviewRepository.getReviewsByProductId(product.productId)
        _uiState.postValue(_uiState.value?.copy(loading = false, reviews = reviews))
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
        val reviews: List<Review> = emptyList()
    )

    /**
     * Event definition for the product view model.
     */
    sealed interface Event {
        data object ReviewSaved: Event
    }
}