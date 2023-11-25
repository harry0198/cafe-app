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
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.data.model.UserModel
import me.harrydrummond.cafeapplication.databinding.FragmentMenuBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.menu.product.ProductListViewAdapter

class MenuFragment : Fragment() {

    private lateinit var adapter: ProductListViewAdapter
    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding
    private var userModel: UserModel? = null

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

        userModel = (activity as AppActivity).userModel
        adapter =  ProductListViewAdapter(this.requireContext(), viewModel.productList.value!!, userModel!!)
        binding.listProducts.adapter = adapter


        when (userModel?.role) {
            Role.EMPLOYEE -> binding.btnAddProduct.isVisible = true
            else -> binding.btnAddProduct.isVisible = false
        }

        viewModel.productList.observe(this.viewLifecycleOwner) {items ->
            adapter.productList = items
            adapter.notifyDataSetChanged()
        }
        onAddMenuItemButtonListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }

    private fun onAddMenuItemButtonListener(){
        binding.btnAddProduct.setOnClickListener {
            viewModel.addProduct()
        }
    }
}