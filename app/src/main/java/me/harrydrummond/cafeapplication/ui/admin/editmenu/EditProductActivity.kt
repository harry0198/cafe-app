package me.harrydrummond.cafeapplication.ui.admin.editmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.databinding.ActivityEditProductBinding
import me.harrydrummond.cafeapplication.databinding.FragmentEditMenuBinding
import me.harrydrummond.cafeapplication.ui.State
import me.harrydrummond.cafeapplication.ui.common.register.profile.CreateProfileViewModel

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

        bindings()

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
    }

    fun onSaveBtnClicked(view: View) {
        // Validation
        val name = binding.editProductTitle.text.toString()
        val desc = binding.editProductDesc.text.toString()
        val price = binding.editProductPrice.text.toString()
        val availability = binding.editProductAvailability.isChecked

        val priceSanitized = price.toDouble()

        viewModel.saveProduct(name, desc, priceSanitized, availability)
    }

    fun onDeleteBtnClicked(view: View) {
        viewModel.deleteProduct()
    }

    private fun performingTask(performing: Boolean) {
        binding.epProgress.isVisible = performing
        binding.btnDeleteProduct.isEnabled = !performing
        binding.btnSaveProduct.isEnabled = !performing
    }

    private fun bindings() {
        viewModel.uiProcessingState.observe(this) {
            when (it) {
                State.SUCCESS -> {
                    performingTask(false)
                    Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show()
                }
                State.FAILURE -> {
                    performingTask(false)
                    Toast.makeText(this, "Unable to Update Product", Toast.LENGTH_SHORT).show()
                }
                State.PROCESSING -> {
                    performingTask(true)
                }
                else -> {
                    performingTask(false)
                }
            }
        }

        viewModel.productDeleted.observe(this) {
            if (it) {
                onBackPressed()
            }
        }
    }
}