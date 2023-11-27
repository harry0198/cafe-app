package me.harrydrummond.cafeapplication.ui.admin.editmenu

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.databinding.FragmentEditMenuBinding
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.common.productview.ProductListViewAdapter
import me.harrydrummond.cafeapplication.ui.customer.menu.product.ProductViewActivity

class EditMenuFragment : Fragment() {

    private lateinit var binding: FragmentEditMenuBinding
    private lateinit var viewModel: EditMenuViewModel
    private lateinit var adapter: ProductListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditMenuViewModel::class.java)

        adapter =  ProductListViewAdapter(this.requireContext(), viewModel.productList.value!!) { product ->
            val intent = Intent(requireContext(), EditProductActivity::class.java).apply {
                putExtra(IntentExtra.PRODUCT, product)
            }

            startActivity(intent)
        }
        binding.listProducts.adapter = adapter
        viewModel.productList.observe(this.viewLifecycleOwner) {items ->
            adapter.productList = items
            adapter.notifyDataSetChanged()
        }

        onAddMenuItemButtonListener()
    }

    private fun onAddMenuItemButtonListener() {
        binding.btnAddProduct.setOnClickListener {
            viewModel.addProduct()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }
}