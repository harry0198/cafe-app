package me.harrydrummond.cafeapplication.ui.menu

import android.content.DialogInterface.OnClickListener
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.menu.product.ProductListViewAdapter

class MenuFragment : Fragment() {

    private lateinit var adapter: ProductListViewAdapter
    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddMenuItem.isVisible = true

        adapter =  ProductListViewAdapter(this.requireContext(), viewModel.productList.value!!)
        binding.listProducts.adapter = adapter

        viewModel.uiButtonState.observe(this.viewLifecycleOwner) { state ->
            when (state) {
                ButtonUIState.VISIBLE -> binding.btnAddMenuItem.isVisible = true
                else -> binding.btnAddMenuItem.isVisible = false
            }
        }

        onAddMenuItemButtonListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }

    private fun onAddMenuItemButtonListener(){
        binding.btnAddMenuItem.setOnClickListener {
            val productId = viewModel.addProduct()

            if (productId == -1L) {
                // Error
                return@setOnClickListener
            }

            adapter.notifyDataSetChanged()
            Toast.makeText(this.context, "New menu item added", Toast.LENGTH_SHORT).show()
        }
    }
}