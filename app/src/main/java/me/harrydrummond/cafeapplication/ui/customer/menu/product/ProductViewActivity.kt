package me.harrydrummond.cafeapplication.ui.customer.menu.product

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.databinding.ActivityProductViewBinding
import me.harrydrummond.cafeapplication.logic.toBitmap
import me.harrydrummond.cafeapplication.logic.toPrice
import me.harrydrummond.cafeapplication.ui.common.reviews.ReviewListViewAdapter
import me.harrydrummond.cafeapplication.ui.common.reviews.ViewReviewsActivity
import me.harrydrummond.cafeapplication.ui.common.reviews.ViewReviewsViewModel

/**
 * ProductViewActivity class.
 * This is the View for the MVVM pattern. Sends events to the OrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see ProductViewModel
 * @see ActivityProductViewBinding
 * @author Harry Drummond
 */
class ProductViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductViewBinding
    private lateinit var viewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        setSupportActionBar(binding.prodToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // This is asserted because if no product was passed, we can't display anything anyway.
        // It is not this class' responsibility to resolve the issue.
        val product = intent.getParcelableExtra(IntentExtra.PRODUCT, Product::class.java)!!
        viewModel.initialize(product)

        binding.lblProductName.text = product.productName
        binding.lblProductPrice.text = product.productPrice.toPrice()
        binding.lblProductDescription.text = product.productDescription
        binding.imageView2.setImageBitmap(product.productImage?.toBitmap(500, 500))

        handleUIState()
    }

    /**
     * Event handler for the increment button.
     */
    fun onIncrementBtnClicked(view: View) {
        viewModel.incrementQuantity()
    }

    /**
     * Event handler for the decrement button.
     */
    fun onDecrementBtnClicked(view: View) {
        viewModel.decrementQuantity()
    }

    /**
     * Event handler for the add to cart button.
     * Adds the current product to the user's cart.
     */
    fun onAddToCartButtonClicked(view: View) {
        viewModel.addToCart()
    }

    /**
     * When the view reviews button is clicked.
     * Starts view reviews activity
     */
    fun onViewReviewBtnClicked(view: View) {
        val intent = Intent(this, ViewReviewsActivity::class.java).apply {
            putExtra(IntentExtra.PRODUCT, viewModel.product)
        }
        startActivity(intent)
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.pvProgress.isVisible = uiState.loading
            binding.lblQuantity.text = uiState.quantity.toString()

            if (uiState.event != null) {
                when (uiState.event) {
                    Event.ItemAddedToCart -> {
                        Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        viewModel.eventExecuted()
                        return@observe
                    }
                }
            }

            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }
        }
    }
}