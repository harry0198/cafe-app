package me.harrydrummond.cafeapplication.ui.common.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.harrydrummond.cafeapplication.R

/**
 * View class for the CartItemListViewAdapter
 *
 * @see CartItemListViewAdapter
 */
class CartItemListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list_view)
    }
}