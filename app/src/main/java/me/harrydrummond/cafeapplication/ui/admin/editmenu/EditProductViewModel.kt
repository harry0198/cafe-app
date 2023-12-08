package me.harrydrummond.cafeapplication.ui.admin.editmenu

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.harrydrummond.cafeapplication.logic.validators.ValidatedResult
import me.harrydrummond.cafeapplication.logic.validators.Validators
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.logic.toByteArray
import javax.inject.Inject

/**
 * EditProductViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh orders and handle the ui state.
 *
 * @see EditProductActivity
 * @author Harry Drummond
 */
@HiltViewModel
class EditProductViewModel @Inject constructor(private val productRepository: IProductRepository): ViewModel() {

    private val _uiState: MutableLiveData<EditProductUIState> = MutableLiveData(EditProductUIState())
    val uiState: LiveData<EditProductUIState> get() = _uiState
    lateinit var productModel: Product

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
    fun saveProduct(productName: String, productDesc: String, productPrice: Double, productAvailable: Boolean, image: Bitmap?) {

        // Ensure is validated
        val validated = validate(productName, productDesc, productPrice)
        if (!validated) return

        // Set attributes
        productModel.productName = productName
        productModel.productDescription = productDesc
        productModel.productPrice = productPrice
        productModel.productAvailable = productAvailable
        productModel.productImage = image?.toByteArray()

        // In background update the product and notify view
        _uiState.value = _uiState.value?.copy(loading = true)
        viewModelScope.launch {
            val updated = productRepository.update(productModel)
            if (updated) {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.ProductSaved))
            } else {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Unable to update product"))
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

        // In background, delete product and notify view.
        viewModelScope.launch {
            val deleted = productRepository.delete(productModel)
            if (deleted) {
                _uiState.postValue(_uiState.value?.copy(loading = false, event = Event.ProductDeleted))
            } else {
                _uiState.postValue(_uiState.value?.copy(loading = false, errorMessage = "Unable to delete product"))
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

    private fun validate(productName: String, productDesc: String, productPrice: Double): Boolean {
        // Validate the inputs
        val name = Validators.validateNotEmpty(productName)
        val desc = Validators.validateNotEmpty(productDesc)
        val price = Validators.validatePrice(productPrice)

        _uiState.postValue(_uiState.value?.copy(validatedProductName = name, validatedPrice = price, validatedProductDesc = desc))

        return name.isValid && desc.isValid && price.isValid
    }

    /**
     * Data class for showing the EditProduct UIState.
     */
    data class EditProductUIState(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val event: Event? = null,
        val validatedProductName: ValidatedResult = ValidatedResult(true, null),
        val validatedProductDesc: ValidatedResult = ValidatedResult(true, null),
        val validatedPrice: ValidatedResult = ValidatedResult(true, null)
    )

    /**
     * Event interface for declaring events to the UI.
     */
    sealed interface Event {
        data object ProductSaved: Event
        data object ProductDeleted: Event
    }
}

