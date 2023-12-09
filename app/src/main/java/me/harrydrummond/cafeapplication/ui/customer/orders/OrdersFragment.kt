package me.harrydrummond.cafeapplication.ui.customer.orders

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.databinding.FragmentOrdersBinding
import me.harrydrummond.cafeapplication.ui.CustomerAppViewModel
import me.harrydrummond.cafeapplication.ui.common.order.OrderListViewAdapter

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
@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var adapter: OrderListViewAdapter
    private lateinit var viewModel: OrdersViewModel
    private lateinit var appViewModel: CustomerAppViewModel
    private lateinit var binding: FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
        appViewModel = ViewModelProvider(requireActivity()).get(CustomerAppViewModel::class.java) // Get shared viewmodel from parent.
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
        adapter =  OrderListViewAdapter(this.requireContext(), emptyList()) { order ->
            val collectedOrder = viewModel.tryCollectOrder(order)

            // On click of an order list view, send to order details activity
            val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
            intent.putExtra(IntentExtra.ORDER_OBJ, collectedOrder)
            startActivity(intent)
        }
        binding.ordersList.adapter = adapter

        handleUIState()
    }

    override fun onResume() {
        super.onResume()
        appViewModel.refreshOrders()
        adapter.notifyDataSetChanged()
    }

    private fun handleUIState() {
        appViewModel.orders.observe(viewLifecycleOwner) {
            adapter.orderItems = it
            adapter.notifyDataSetChanged()
        }
    }
}