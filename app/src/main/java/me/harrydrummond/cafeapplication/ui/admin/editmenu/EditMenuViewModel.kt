package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import javax.inject.Inject

/**
 * EditMenuViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see EditMenuFragment
 * @author Harry Drummond
 */
class EditMenuViewModel @Inject constructor(private val productRepository: IProductRepository): ViewModel() {

    private val _uiState: MutableLiveData<EditMenuUIState> = MutableLiveData(EditMenuUIState())
    val uiState: LiveData<EditMenuUIState> get() = _uiState

    fun refreshProducts() {
        _uiState.value = _uiState.value?.copy(loading = true)

        viewModelScope.launch {
            val allProducts = productRepository.getAllProducts()
            _uiState.postValue(_uiState.value?.copy(loading = false, products = allProducts))
        }
    }

    /**
     * Adds a new dummy product to the database with its availability set to false.
     * Updates the UI View with the changes.
     */
    fun addProduct() {
        val dummyName = "New Product"
        val dummyDescription = "A new product for our menu! Check back here later!"
        val dummyPrice = 3.15
        val dummyImage = ""
        val dummyAvailability = false

        val dummyProduct = Product(-1,
            dummyName,
            dummyPrice,
            dummyImage,
            dummyDescription,
            dummyAvailability
        )

        // In background, add the dummy product
        viewModelScope.launch {
            val saved = productRepository.save(dummyProduct)
            if (saved == -1) {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Unable to save product"))
            } else {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.NewProductAdded))
            }
        }
    }

    /**
     * Indicates that the view has shown the error message and uistate to be updated
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Indicates that the event has been handled and ui state to be updated.
     */
    fun eventHandled() {
        _uiState.value = _uiState.value?.copy(event = null)
    }

    /**
     * Data class for the Edit Menu UI State.
     */
    data class EditMenuUIState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val products: List<Product> = emptyList(),
        val event: Event? = null,
    )

    /**
     * Signature for posting events to the UI.
     */
    sealed interface Event {
        data object NewProductAdded: Event
    }
}