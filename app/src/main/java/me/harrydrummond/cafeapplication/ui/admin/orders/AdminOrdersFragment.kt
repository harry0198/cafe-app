package me.harrydrummond.cafeapplication.ui.admin.orders

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.databinding.ActivityLoginBinding
import me.harrydrummond.cafeapplication.databinding.FragmentAdminOrdersBinding
import me.harrydrummond.cafeapplication.ui.AdminAppViewModel
import me.harrydrummond.cafeapplication.ui.common.login.LoginViewModel
import me.harrydrummond.cafeapplication.ui.customer.orders.OrderDetailsActivity
import me.harrydrummond.cafeapplication.ui.common.order.OrderListViewAdapter

/**
 * AdminOrdersFragment class.
 * This is the View for the MVVM pattern. Sends events to the AdminOrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see OrderListViewAdapter
 * @see AdminOrdersViewModel
 * @see FragmentAdminOrdersBinding
 * @author Harry Drummond
 */
@AndroidEntryPoint
class AdminOrdersFragment : Fragment() {

    private lateinit var adapter: OrderListViewAdapter
    private lateinit var viewModel: AdminOrdersViewModel
    private lateinit var appViewModel: AdminAppViewModel
    private lateinit var binding: FragmentAdminOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminOrdersViewModel::class.java)
        appViewModel = ViewModelProvider(requireActivity()).get(AdminAppViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderListViewAdapter(this.requireContext(), emptyList()) { order ->
            // On click of the order, start the view order activity.
            val intent = Intent(requireContext(), AdminViewOrderActivity::class.java)
            intent.putExtra(IntentExtra.ORDER_OBJ, order)
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
        appViewModel.orders.observe(requireActivity()) { orders ->
            adapter.orderItems = orders
            adapter.notifyDataSetChanged()
        }
    }

}