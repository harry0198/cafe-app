package me.harrydrummond.cafeapplication.ui.customer.cart

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.FragmentCartBinding
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.State
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var adapter: CartItemListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        adapter =  CartItemListViewAdapter(this.requireContext(), listOf(), { product ->
            // Edit Listener
            popupEditQuantity("Edit Amount", 1) { result ->
                viewModel.updateQuantity(product.productId, result)
            }
        }, { product ->
            viewModel.updateQuantity(product.productId, 0)
        })
        binding.cartOrderList.adapter = adapter

        viewModel.cartItems.observe(this.viewLifecycleOwner) {items ->
            adapter.cartItems = items
            adapter.notifyDataSetChanged()
        }

        viewModel.orderPlaceStatus.observe(this.viewLifecycleOwner) { status ->
            when (status) {
                State.SUCCESS -> {
                    viewModel.orderPlaceStatus.value = State.NONE
                    Toast.makeText(requireContext(), "Order Placed", Toast.LENGTH_SHORT).show()
                    (activity as AppActivity).navigateToOrders()
                }
                State.PROCESSING -> {
                    binding.progressBar.isVisible = true
                }
                State.FAILURE -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), "Unable to place order", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.isVisible = false
                }
            }
        }

        binding.btnPlaceOrder.setOnClickListener {
            viewModel.placeOrder()
        }

        viewModel.refreshCart()
    }

    private fun popupEditQuantity(title: String, initialValue: Int, onPositiveButtonClick: (Int) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.edit_number_layout, null)
        val editNumber = dialogLayout.findViewById<EditText>(R.id.enEditNumber)

        editNumber.setText(initialValue.toString())
        with(builder) {
            setTitle(title)
            setPositiveButton("Save") { _, _ ->
                val num = editNumber.text.toString().toIntOrNull()
                val toastText = if (num != null) {
                    onPositiveButtonClick(num.toInt())
                    "Quantity Updated"
                } else {
                    "Invalid quantity specified"
                }
                val toast = Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT)
                toast.show()
            }
            setNegativeButton("Cancel") { dialog, which ->
                // do nothing
            }
            setView(dialogLayout)
            show()
        }
    }

}