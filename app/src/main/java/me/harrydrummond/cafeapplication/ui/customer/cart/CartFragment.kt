package me.harrydrummond.cafeapplication.ui.customer.cart

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.logic.validators.Validators
import me.harrydrummond.cafeapplication.databinding.FragmentCartBinding
import me.harrydrummond.cafeapplication.logic.toPrice
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
@AndroidEntryPoint
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
                viewModel.updateQuantity(product, result)
            }
        }, { product ->
            viewModel.updateQuantity(product, 0)
        })
        binding.cartOrderList.adapter = adapter

        binding.btnPlaceOrder.setOnClickListener {
            onBtnPlaceOrderClicked()
        }

        handleUIState()

        viewModel.refreshCart()
    }

    private fun onBtnPlaceOrderClicked() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.bottom_sheet_payment_layout)

        val price = bottomSheet.findViewById<TextView>(R.id.lblCost)!!
        val cardNo = bottomSheet.findViewById<EditText>(R.id.txtCardNo)!!
        val cardExpiry = bottomSheet.findViewById<EditText>(R.id.txtExpiry)!!
        val cardCVV = bottomSheet.findViewById<EditText>(R.id.txtCVV)!!
        val placeOrderBtn = bottomSheet.findViewById<Button>(R.id.btnPay)!!

        price.text = viewModel.getTotalCost().toPrice()

        placeOrderBtn.setOnClickListener {
            if (viewModel.uiState.value?.cartProducts?.isEmpty() == true) {
                emptyCart()
                return@setOnClickListener
            }
            val validatedCardNo = Validators.validateCardNo(cardNo.text.toString())
            val validatedCardExpiry = Validators.validateExpiry(cardExpiry.text.toString())
            val validatedCVV = Validators.validateCVV(cardCVV.text.toString())

            Validators.apply(validatedCardNo, cardNo)
            Validators.apply(validatedCardExpiry, cardExpiry)
            Validators.apply(validatedCVV, cardCVV)

            if (!validatedCVV.isValid || !validatedCardNo.isValid || !validatedCardExpiry.isValid) {
                return@setOnClickListener
            }

            bottomSheet.dismiss()
            viewModel.placeOrder(cardNo.text.toString(), cardExpiry.text.toString(), cardCVV.text.toString())
        }

        bottomSheet.show()
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
                    Event.CartEmpty -> {
                        emptyCart()
                        return@observe
                    }
                }
            }
        }
    }

    private fun emptyCart() {
        Toast.makeText(requireContext(), "Please add an item to your cart", Toast.LENGTH_SHORT).show()
        viewModel.eventHandled()
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
                val toastText = if (num != null && num <= 15 && num > 0) {
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