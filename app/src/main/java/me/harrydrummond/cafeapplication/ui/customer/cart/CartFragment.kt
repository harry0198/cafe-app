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
import me.harrydrummond.cafeapplication.ui.common.order.CartItemListViewAdapter

/**
 * CartFragment class.
 * This is the View for the MVVM pattern. Sends events to the OrdersViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see CartItemListViewAdapter
 * @see CartViewModel
 * @see FragmentCartBinding
 * @author Harry Drummond
 */
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
            // Edit Listener to update the product quantity.
            popupEditQuantity("Edit Amount", 1) { result ->
                viewModel.updateQuantity(product.productId, result)
            }
        }, { product ->
            viewModel.updateQuantity(product.productId, 0)
        })
        binding.cartOrderList.adapter = adapter

        binding.btnPlaceOrder.setOnClickListener {
            viewModel.placeOrder()
        }

        handleUIState()

        viewModel.refreshCart()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(requireActivity()) { uiState ->
            binding.progressBar.isVisible = uiState.loading

            adapter.cartItems = uiState.cartProducts
            adapter.notifyDataSetChanged()

            if (uiState.errorMessage != null) {
                Toast.makeText(requireContext(), uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
                return@observe
            }
            if (uiState.event != null) {
                when (uiState.event) {
                    Event.OrderPlaced -> {
                        Toast.makeText(requireContext(), "Order placed", Toast.LENGTH_SHORT).show()
                        viewModel.eventHandled()
                        return@observe
                    }
                }
            }
        }
    }

    private fun popupEditQuantity(title: String, initialValue: Int, onPositiveButtonClick: (Int) -> Unit) {
        // Create a popup
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