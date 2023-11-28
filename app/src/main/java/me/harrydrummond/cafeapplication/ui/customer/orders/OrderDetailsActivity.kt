package me.harrydrummond.cafeapplication.ui.customer.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.databinding.ActivityOrderDetailsBinding
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter

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

        val productId = intent.getStringExtra(IntentExtra.ORDER_ID)

        // No product id is fatal. There's no way this view can be shown.
        viewModel.initialize(productId!!)

        bindings()
    }

    private fun bindings() {
        viewModel.products.observe(this) { items ->
            adapter.cartItems = items
            adapter.notifyDataSetChanged()
        }
    }
}