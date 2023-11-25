package me.harrydrummond.cafeapplication.ui.menu.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.ActivityProductViewBinding
import me.harrydrummond.cafeapplication.model.Role
import me.harrydrummond.cafeapplication.ui.AbstractAuthenticatedActivity

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

    fun onDeleteButtonClicked(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this?").setPositiveButton("Delete") { _, _ ->
            val deleted = productViewModel.deleteProduct()
            if (deleted) {
                val toast = Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT)
                toast.show()
                finish()
            } else {
                val toast = Toast.makeText(this, "An unknown error occured", Toast.LENGTH_SHORT)
                toast.show()

            }
        }.setNegativeButton("Cancel") { _, _ ->
                // Nothing
        }

        builder.show()
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
        popupEditDecimal("Edit price", productViewModel.productPrice.value ?: 0.0) { price ->
            // Our view will handle cases that are not a number. No need for it here.
            productViewModel.saveProductPrice(price.toDouble())
        }
    }

    fun onAvailabilityToggle(view: View) {
        productViewModel.saveProductAvailability(binding.availabilityToggle.isChecked)
    }

    private fun popupEditDecimal(title: String, initialValue: Double, onPositiveButtonClick: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_number_layout, null)
        val editNumber = dialogLayout.findViewById<EditText>(R.id.enEditNumber)

        val toast = Toast.makeText(this, "Field Updated", Toast.LENGTH_SHORT)
        editNumber.setText(initialValue.toString())
        with(builder) {
            setTitle(title)
            setPositiveButton("Save") { _, _ ->
                onPositiveButtonClick(editNumber.text.toString())
                toast.show()
            }
            setNegativeButton("Cancel") { dialog, which ->
                // do nothing
            }
            setView(dialogLayout)
            show()
        }
    }

    private fun popupEditText(title: String, initialValue: String, onPositiveButtonClick: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etLayout)

        val toast = Toast.makeText(this, "Field Updated", Toast.LENGTH_SHORT)
        editText.setText(initialValue)

        with(builder) {
            setTitle(title)
            setPositiveButton("Save") { _, _ ->
                    onPositiveButtonClick(editText.text.toString())
                    toast.show()
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
        binding.btnDeleteProduct.isVisible = isEmployee
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
        productViewModel.productAvailability.observe(this) { toggle ->
            binding.availabilityToggle.isChecked = toggle
        }
    }
}