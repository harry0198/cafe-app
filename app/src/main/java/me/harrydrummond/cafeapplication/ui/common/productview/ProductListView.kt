package me.harrydrummond.cafeapplication.ui.common.productview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.harrydrummond.cafeapplication.R

/**
 * View class for the ProductListViewAdapter
 *
 * @see ProductListViewAdapter
 */
class ProductListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list_view)
    }
}