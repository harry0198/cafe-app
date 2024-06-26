package me.harrydrummond.cafeapplication.ui.admin.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.model.Status
import me.harrydrummond.cafeapplication.databinding.ActivityAdminViewOrderBinding
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter

/**
 * AdminViewOrderActivity class.
 * This is the View for the MVVM pattern. Sends events to the AdminViewOrderViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see CartItemListViewAdapter
 * @see AdminViewOrderViewModel
 * @see ActivityAdminViewOrderBinding
 * @author Harry Drummond
 */
@AndroidEntryPoint
class AdminViewOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminViewOrderBinding
    private lateinit var viewModel: AdminViewOrderViewModel
    private lateinit var adapter: CartItemListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminViewOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AdminViewOrderViewModel::class.java)
        adapter = CartItemListViewAdapter(this, listOf(), null, null)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.detailListView.adapter = adapter

        val order = intent.getParcelableExtra(IntentExtra.ORDER_OBJ, Order::class.java)

        // No product id is fatal. There's no way this view can be shown.
        viewModel.initialize(order!!)

        handleUIState()
    }

    /**
     * Event handler for when the preparing button was clicked.
     * Changes the order status to preparing.
     */
    fun onPreparingBtnClicked(view: View) {
        viewModel.changeOrderStatus(Status.PREPARING)
    }

    /**
     * Event handler for when the ready button was clicked.
     * Changes the order status to ready.
     */
    fun onReadyBtnClicked(view: View) {
        viewModel.changeOrderStatus(Status.READY)
    }

    private fun handleUIState() {
        viewModel.orderUiState.observe(this) { uiState ->
            binding.progressBar.isVisible = uiState.isLoading
            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }
            when (uiState.orderStatus) {
                Status.READY -> binding.lblStatus.text = "Ready for Collection"
                Status.PREPARING -> binding.lblStatus.text = "Preparing..."
                Status.RECEIVED -> binding.lblStatus.text = "Received"
                Status.COLLECTED -> binding.lblStatus.text = "Collected"
                Status.NONE -> binding.lblStatus.text = "Received"
            }
            adapter.cartItems = uiState.productData
            adapter.notifyDataSetChanged()

        }
    }
}