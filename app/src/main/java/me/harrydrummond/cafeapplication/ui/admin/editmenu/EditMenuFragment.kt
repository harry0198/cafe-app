package me.harrydrummond.cafeapplication.ui.admin.editmenu

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.FragmentEditMenuBinding
import me.harrydrummond.cafeapplication.ui.common.productview.ProductListViewAdapter

/**
 * EditMenuFragment class.
 * This is the View for the MVVM pattern. Sends events to the EditMenuViewModel.
 * Contains functions to update the UI based on the ViewModel bindings and button event handlers.
 *
 * @see ProductListViewAdapter
 * @see EditMenuViewModel
 * @see FragmentEditMenuBinding
 * @author Harry Drummond
 */
@AndroidEntryPoint
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

        adapter =  ProductListViewAdapter(this.requireContext(), emptyList()) { product ->
            // On product selected, send to editproductactivity.
            val intent = Intent(requireContext(), EditProductActivity::class.java).apply {
                putExtra(IntentExtra.PRODUCT, product)
            }

            startActivity(intent)
        }
        binding.listProducts.adapter = adapter
        onAddMenuItemButtonListener()
        onSendPromotionButtonClicked()

        handleUIState()
    }


    private fun onSendPromotionButtonClicked() {
        binding.btnSendPromotion.setOnClickListener {
            popupPromotionalMessage("Send Notification", "")
        }
    }

    private fun onAddMenuItemButtonListener() {
        binding.btnAddProduct.setOnClickListener {
            viewModel.addProduct()
        }
    }

    private fun popupPromotionalMessage(title: String, initialValue: String) {
        // Create a popup
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etLayout)

        editText.setText(initialValue)
        with(builder) {
            setTitle(title)
            setPositiveButton("Send Message") { _, _ ->
                val message = editText.text.toString()
                viewModel.sendPromotional(message)
            }
            setNegativeButton("Cancel") { dialog, which ->
                // do nothing
            }
            setView(dialogLayout)
            show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }

    private fun handleUIState() {
        viewModel.uiState.observe(requireActivity()) { uiState ->
            binding.progressBar.isVisible = uiState.loading
            adapter.productList = uiState.products
            adapter.notifyDataSetChanged()

            if (uiState.errorMessage != null) {
                Toast.makeText(requireContext(), uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
                return@observe
            }

            if (uiState.event != null) {
                when (uiState.event) {
                    EditMenuViewModel.Event.NewProductAdded -> {
                        Toast.makeText(requireContext(), "New Product Added", Toast.LENGTH_SHORT).show()
                        viewModel.eventHandled()
                    }
                }

            }
        }
    }
}