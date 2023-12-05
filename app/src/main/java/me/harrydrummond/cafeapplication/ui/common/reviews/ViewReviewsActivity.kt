package me.harrydrummond.cafeapplication.ui.common.reviews

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.databinding.ActivityViewReviewsBinding

@AndroidEntryPoint
class ViewReviewsActivity : AppCompatActivity() {

    private lateinit var adapter: ReviewListViewAdapter
    private lateinit var binding: ActivityViewReviewsBinding
    private lateinit var viewModel: ViewReviewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ViewReviewsViewModel::class.java)

        adapter =  ReviewListViewAdapter(this, emptyList())
        binding.reviewsList.adapter = adapter

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // This is asserted because if no product was passed, we can't display anything anyway.
        // It is not this class' responsibility to resolve the issue.
        val product = intent.getParcelableExtra(IntentExtra.PRODUCT, Product::class.java)!!
        viewModel.initialize(product)

        handleUIState()
    }

    /**
     * When the up button is pressed, instead of going back to a parent activity,
     * behave like the back button. This allows the view to be used with both admin and customer.
     *
     * @see https://stackoverflow.com/questions/22947713/make-the-up-button-behave-like-the-back-button-on-android
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle the Up button like the Back button
                return true
            }
            // Add other menu item handling here if needed
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Event handler for the write review button.
     * Prompts the user to write a brief review
     */
    fun onWriteReviewBtnClicked(view: View) {
        popupAddReview("Write a Review", "In my opinion...") { review ->
            viewModel.saveReview(review)
        }
    }

    private fun handleUIState() {
        viewModel.uiState.observe(this) { uiState ->
            binding.progressBar.isVisible = uiState.loading

            adapter.reviewList = uiState.reviews
            adapter.notifyDataSetChanged()

            if (uiState.event != null) {
                when (uiState.event) {
                    ViewReviewsViewModel.Event.ReviewSaved-> {
                        Toast.makeText(this, "Review Saved", Toast.LENGTH_SHORT).show()
                        viewModel.eventExecuted()
                        return@observe
                    }
                }
            }

            if (uiState.errorMessage != null) {
                Toast.makeText(this, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.errorMessageShown()
            }
        }
    }

    private fun popupAddReview(title: String, initialValue: String, onPositiveButtonClick: (String) -> Unit) {
        // Create a popup
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etLayout)

        editText.setText(initialValue)
        with(builder) {
            setTitle(title)
            setPositiveButton("Save") { _, _ ->
                val review = editText.text.toString()
                onPositiveButtonClick(review)
            }
            setNegativeButton("Cancel") { dialog, which ->
                // do nothing
            }
            setView(dialogLayout)
            show()
        }
    }
}