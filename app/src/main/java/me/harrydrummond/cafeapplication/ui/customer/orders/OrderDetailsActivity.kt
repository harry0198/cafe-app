package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.databinding.ActivityOrderDetailsBinding
import me.harrydrummond.cafeapplication.databinding.FragmentOrdersBinding
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter
import me.harrydrummond.cafeapplication.ui.common.order.OrderListViewAdapter
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * OrderDetailsActivity class.
 * This is the View for the MVVM pattern. Sends events to the OrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see CartItemListViewAdapter
 * @see OrderDetailsViewModel
 * @see ActivityOrderDetailsBinding
 * @author Harry Drummond
 */
class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var viewModel: OrderDetailsViewModel
    private lateinit var adapter: CartItemListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        adapter =  CartItemListViewAdapter(this, listOf(), null, null)

        setSupportActionBar(binding.odToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.detailListView.adapter = adapter

        val productId = intent.getParcelableExtra(IntentExtra.ORDER_OBJ, Order::class.java)

        // No product id is fatal. There's no way this view can be shown.
        viewModel.initialize(productId!!)

        handleUIState()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            if (uiState.dateOfOrder != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK)
                binding.lblDateOfOrder.text = sdf.format(uiState.dateOfOrder)
            }

            binding.odProgress.isVisible = uiState.loading

            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }

            adapter.cartItems = uiState.productsAndQuantity
            adapter.notifyDataSetChanged()
        }
    }

}