package me.harrydrummond.cafeapplication.ui.admin.editmenu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.databinding.ActivityEditProductBinding
import me.harrydrummond.cafeapplication.logic.toBitmap
import me.harrydrummond.cafeapplication.ui.common.reviews.ViewReviewsActivity


/**
 * EditProductActivity class.
 * This is the View for the MVVM pattern. Sends events to the EditProductViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see EditProductViewModel
 * @see ActivityEditProductBinding
 * @author Harry Drummond
 */
@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProductBinding
    lateinit var viewModel: EditProductViewModel
    private var image: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(EditProductViewModel::class.java)

        setSupportActionBar(binding.epToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val product = intent.getParcelableExtra(IntentExtra.PRODUCT, Product::class.java)
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
        val image = product.productImage?.toBitmap(400, 400)
        binding.productImage.setImageBitmap(image)
        this.image = image

        // Adapted https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        // to suit usecase
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data

                if (imageUri != null) {

                    val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    binding.productImage.setImageBitmap(bitmap)
                    this.image = bitmap
                }
            }
        }
        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            resultLauncher.launch(intent)
        }

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

        viewModel.saveProduct(name, desc, priceSanitized, availability, image)
    }

    /**
     * Event handler for the delete button.
     * Deletes the product.
     */
    fun onDeleteBtnClicked(view: View) {
        viewModel.deleteProduct()
    }

    /**
     * Event handler for the view reviews button.
     * Navigates to the view review activity
     */
    fun onAdminViewReviewBtnClicked(view: View) {
        val intent = Intent(this, ViewReviewsActivity::class.java).apply {
            putExtra(IntentExtra.PRODUCT, viewModel.productModel)
        }
        startActivity(intent)
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
                        onBackPressed()
                        viewModel.eventHandled()
                    }
                    EditProductViewModel.Event.ProductSaved -> {
                        Toast.makeText(this, "Product Saved", Toast.LENGTH_SHORT).show()
                        viewModel.eventHandled()
                    }
                }
            }
        }
    }
}