package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.databinding.ActivityEditProductBinding

/**
 * EditProductActivity class.
 * This is the View for the MVVM pattern. Sends events to the EditProductViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see EditProductViewModel
 * @see ActivityEditProductBinding
 * @author Harry Drummond
 */
class EditProductActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProductBinding
    lateinit var viewModel: EditProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(EditProductViewModel::class.java)

        setSupportActionBar(binding.epToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val product = intent.getParcelableExtra(IntentExtra.PRODUCT, ProductModel::class.java)
        if (product == null) {
            Toast.makeText(this, "A Fatal Error Occurred", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.init(product)
        binding.editProductTitle.setText(product.productName)
        binding.editProductDesc.setText(product.productDescription)
        binding.editProductPrice.setText(product.productPrice.toString())
        binding.editProductAvailability.isChecked = product.productAvailable

        handleUIState()
    }

    /**
     * Event handler for the save button. Updates the view fields and saves.
     */
    fun onSaveBtnClicked(view: View) {
        // Validation
        val name = binding.editProductTitle.text.toString()
        val desc = binding.editProductDesc.text.toString()
        val price = binding.editProductPrice.text.toString()
        val availability = binding.editProductAvailability.isChecked

        val priceSanitized = price.toDouble()

        viewModel.saveProduct(name, desc, priceSanitized, availability)
    }

    /**
     * Event handler for the delete button.
     */
    fun onDeleteBtnClicked(view: View) {
        viewModel.deleteProduct()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.epProgress.isVisible = uiState.loading

            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
                return@observe
            }

            if (uiState.event != null) {
                when (uiState.event) {
                    EditProductViewModel.Event.ProductDeleted -> {
                        Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT).show()
                        viewModel.eventHandled()
                    }
                    EditProductViewModel.Event.ProductSaved -> {
                        Toast.makeText(this, "Product Saved", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        viewModel.eventHandled()
                    }
                }
            }
        }
    }
}