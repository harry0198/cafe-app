package me.harrydrummond.cafeapplication.ui.customer.menu

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
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.common.productview.ProductListViewAdapter
import me.harrydrummond.cafeapplication.ui.customer.menu.product.ProductViewActivity

/**
 * MenuFragment class.
 * This is the View for the MVVM pattern. Sends events to the OrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see ProductListViewAdapter
 * @see MenuViewModel
 * @see FragmentMenuBinding
 * @author Harry Drummond
 */
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

        // Setup adapter
        adapter =  ProductListViewAdapter(this.requireContext(), emptyList()) { product ->
            // On product click, send to product view activity
            val intent = Intent(requireContext(), ProductViewActivity::class.java).apply {
                putExtra(IntentExtra.PRODUCT, product)
            }

            startActivity(intent)
        }
        binding.listProducts.adapter = adapter

        handleUIState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.progressBar.isVisible = uiState.loading
            if (uiState.errorMessage != null) {
                Toast.makeText(requireContext(), uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }

            // Update product list view
            adapter.productList = uiState.products
            adapter.notifyDataSetChanged()
        }
    }
}