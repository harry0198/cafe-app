package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

/**
 * EditProductViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see EditProductActivity
 * @author Harry Drummond
 */
class EditProductViewModel: ViewModel() {

    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val productRepository: IProductRepository = FirestoreProductRepository(userRepository)
    private lateinit var productModel: Product
    private val _uiState: MutableLiveData<EditProductUIState> = MutableLiveData(EditProductUIState())
    val uiState: LiveData<EditProductUIState> get() = _uiState

    /**
     * This class requires a productmodel.
     * This should be called first.
     *
     * @see Product
     */
    fun init(productModel: Product) {
        this.productModel = productModel
    }

    /**
     * Function to save product to database
     *
     * @param productName Name of product
     * @param productDesc Description of product
     * @param productPrice Price of product
     * @param productAvailable Is the product available to customers?
     */
    fun saveProduct(productName: String, productDesc: String, productPrice: Double, productAvailable: Boolean) {
        _uiState.value = _uiState.value?.copy(loading = true)

        productModel.productName = productName
        productModel.productDescription = productDesc
        productModel.productPrice = productPrice
        productModel.productAvailable = productAvailable

        productRepository.saveProduct(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.ProductSaved)
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Unable to save product")
            }
        }
    }

    /**
     * Deletes this product from the database
     *
     * @see Product
     */
    fun deleteProduct() {
        _uiState.value = _uiState.value?.copy(loading = true)

        productRepository.deleteProduct(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                _uiState.value = _uiState.value?.copy(loading = false, event = Event.ProductDeleted)
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Unable to delete product")
            }
        }
    }

    /**
     * Indictaes that an event has been handled.
     */
    fun eventHandled() {
        _uiState.value = _uiState.value?.copy(event = null)
    }

    /**
     * Indicates that the view has shown the error message.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    /**
     * Data class for showing the EditProduct UIState.
     */
    data class EditProductUIState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val event: Event? = null,
    )

    /**
     * Event interface for declaring events to the UI.
     */
    sealed interface Event {
        data object ProductSaved: Event
        data object ProductDeleted: Event
    }
}

