package me.harrydrummond.cafeapplication.ui.customer.orders

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.databinding.FragmentOrdersBinding
import me.harrydrummond.cafeapplication.ui.common.order.OrderDetailsActivity
import me.harrydrummond.cafeapplication.ui.common.order.OrderListViewAdapter

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

        adapter =  OrderListViewAdapter(this.requireContext(), viewModel.orders.value!!) { product ->
            val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
            intent.putExtra(IntentExtra.ORDER_ID, product.orderId)
            startActivity(intent)
        }
        binding.ordersList.adapter = adapter

        viewModel.orders.observe(this.viewLifecycleOwner) { items ->
            adapter.orderItems = items
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshOrders()
        adapter.notifyDataSetChanged()
    }
}