package me.harrydrummond.cafeapplication.ui.customer.menu.product

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.databinding.ActivityProductViewBinding
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.data.repository.UserRepository
import me.harrydrummond.cafeapplication.ui.State

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
        // It is not this class' responsibility to resolve.
        val product = intent.getParcelableExtra(IntentExtra.PRODUCT, ProductModel::class.java)!!
        viewModel.initialize(product)

        bindings()
    }

    fun onAddToCartButtonClicked(view: View) {
        viewModel.addToCart()
    }

    private fun bindings() {
        viewModel.saveState.observe(this) {
            when (it) {
                State.SUCCESS -> {
                    binding.pvProgress.isVisible = false
                    Toast.makeText(this, "Cart item added", Toast.LENGTH_SHORT).show()
                }
                State.FAILURE -> {
                    binding.pvProgress.isVisible = false
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
                State.PROCESSING -> {
                    binding.pvProgress.isVisible = true
                }
                else -> binding.pvProgress.isVisible = true
            }
        }
    }
}