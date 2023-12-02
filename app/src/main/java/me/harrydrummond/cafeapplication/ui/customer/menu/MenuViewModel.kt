package me.harrydrummond.cafeapplication.ui.customer.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.harrydrummond.cafeapplication.data.repository.FirestoreProductRepository
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.FirestoreOrderRepository
import me.harrydrummond.cafeapplication.data.repository.FirestoreUserRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Inject

/**
 * MenuViewModel class which provides the business logic to the view class
 * using the MVVM pattern. Contains functions to refresh the currently shown products.
 *
 * @see MenuFragment
 * @author Harry Drummond
 */
class MenuViewModel : ViewModel() {

    private val userRepository: IUserRepository = FirestoreUserRepository()
    private val productRepository: IProductRepository = FirestoreProductRepository(userRepository)
    private val _uiState: MutableLiveData<MenuUiState> = MutableLiveData(MenuUiState())
    val uiState: LiveData<MenuUiState> get() = _uiState


    /**
     * Refreshes the products and updates the UI State. Containers handlers for errors.
     */
    fun refreshProducts(){
        _uiState.value = _uiState.value?.copy(loading = true)

        productRepository.getAllAvailableProducts().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiState.value = _uiState.value?.copy(loading = false, products = task.result)
            } else {
                _uiState.value = _uiState.value?.copy(loading = false, errorMessage = "Unable to refresh products")
            }
        }
    }

    /**
     * Indicates that an error message has been shown in the UI and can be removed from UIState.
     */
    fun errorMessageShown() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}

/**
 * Data class for the CreateProfile UI State.
 * Includes fields for loading, errors and events.
 */
data class MenuUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<Product> = emptyList()
)