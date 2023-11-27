package me.harrydrummond.cafeapplication.ui.customer.menu

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.common.productview.ProductListViewAdapter
import me.harrydrummond.cafeapplication.ui.customer.menu.product.ProductViewActivity

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

        adapter =  ProductListViewAdapter(this.requireContext(), viewModel.productList.value!!) { product ->
            val intent = Intent(requireContext(), ProductViewActivity::class.java).apply {
                putExtra(IntentExtra.PRODUCT, product)
            }

            startActivity(intent)
        }
        binding.listProducts.adapter = adapter

        viewModel.productList.observe(this.viewLifecycleOwner) {items ->
            adapter.productList = items
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }
}