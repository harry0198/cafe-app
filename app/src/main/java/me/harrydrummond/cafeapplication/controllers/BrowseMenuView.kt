package me.harrydrummond.cafeapplication.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.adapters.ProductListViewAdapter

class BrowseMenuView : AppCompatActivity() {

    private lateinit var productListView: ListView
    private var productList = arrayOf("Cappucino", "Espresso", "other")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_menu_view)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        productListView = findViewById(R.id.listProducts)
        productListView.adapter = ProductListViewAdapter(this, productList)
    }
}