package me.harrydrummond.cafeapplication.ui.customer.orders

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.databinding.ActivityCreateProfileBinding
import me.harrydrummond.cafeapplication.databinding.FragmentOrdersBinding
import me.harrydrummond.cafeapplication.ui.common.order.OrderListViewAdapter
import me.harrydrummond.cafeapplication.ui.common.profile.CreateProfileViewModel

/**
 * OrdersFragment class.
 * This is the View for the MVVM pattern. Sends events to the OrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see OrderListViewAdapter
 * @see OrdersViewModel
 * @see FragmentOrdersBinding
 * @author Harry Drummond
 */
class OrdersFragment : Fragment() {

    private lateinit var adapter: OrderListViewAdapter
    private lateinit var viewModel: OrdersViewModel
    private lateinit var binding: FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the order list view adapter to be empty
        adapter =  OrderListViewAdapter(this.requireContext(), emptyList()) { product ->
            // On click of an order list view, send to order details activity
            val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
            intent.putExtra(IntentExtra.ORDER_ID, product.orderId)
            startActivity(intent)
        }
        binding.ordersList.adapter = adapter

        handleUIState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshOrders()
        adapter.notifyDataSetChanged()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.progressBar.isVisible = uiState.loading
            if (uiState.errorMessage != null) {
                Toast.makeText(requireContext(), uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }

            adapter.orderItems = uiState.orders
            adapter.notifyDataSetChanged()
        }
    }
}