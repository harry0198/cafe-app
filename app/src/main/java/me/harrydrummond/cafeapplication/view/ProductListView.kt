package me.harrydrummond.cafeapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.harrydrummond.cafeapplication.R

class ProductListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list_view)
    }
}