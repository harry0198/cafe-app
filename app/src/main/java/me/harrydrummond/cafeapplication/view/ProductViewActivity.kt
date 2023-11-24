package me.harrydrummond.cafeapplication.view

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.ActivityProductViewBinding
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.viewmodel.ProductViewModel

class ProductViewActivity : AbstractAuthenticatedActivity() {

    private lateinit var binding: ActivityProductViewBinding
    private lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val productId: Long = intent.getLongExtra("PRODUCT", -1L)
        productViewModel.initialize(productId)

        bindings()
        visibility()
    }

    fun onEditTitleButtonClicked(view: View) {
        popupEditText("Edit product name", productViewModel.productName.value ?: "") { newName ->
            productViewModel.saveProductName(newName)
        }
    }

    fun onEditDescriptionButtonClicked(view: View) {
        popupEditText("Edit product description", productViewModel.productDesc.value ?: "") { newDesc ->
            productViewModel.saveProductDescription(newDesc)
        }
    }

    fun onEditPriceButtonClicked(view: View) {

    }

    fun onAvailabilityToggle(view: View) {

    }

    private fun popupEditText(title: String, initialValue: String, onPositiveButtonClick: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etLayout)

        editText.setText(initialValue)

        with(builder) {
            setTitle(title)
            setPositiveButton("Save") { _, _ ->
                onPositiveButtonClick(editText.text.toString())
            }
            setNegativeButton("Cancel") { dialog, which ->
                // do nothing
            }
            setView(dialogLayout)
            show()
        }
    }


    private fun visibility() {
        val isEmployee = role == Role.EMPLOYEE
        binding.btnEditDescription.isVisible = isEmployee
        binding.availabilityToggle.isVisible = isEmployee
        binding.btnEditPrice.isVisible = isEmployee
        binding.btnEditTitle.isVisible = isEmployee
        binding.counter.isVisible = !isEmployee
        binding.btnAddToCart.isVisible = !isEmployee
        binding.lblTANDC.isVisible = !isEmployee
    }

    private fun bindings() {
        productViewModel.productName.observe(this) { name ->
            binding.lblProductName.text = name
        }
        productViewModel.productPrice.observe(this) { price ->
            binding.lblProductPrice.text = "£${price}"
        }
        productViewModel.productDesc.observe(this) { desc ->
            binding.lblProductDescription.text = desc
        }
    }
}