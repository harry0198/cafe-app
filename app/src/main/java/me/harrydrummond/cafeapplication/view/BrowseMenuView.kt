package me.harrydrummond.cafeapplication.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.adapters.ProductListViewAdapter
import me.harrydrummond.cafeapplication.databinding.ActivityBrowseMenuViewBinding
import me.harrydrummond.cafeapplication.viewmodel.BrowseMenuViewModel
import me.harrydrummond.cafeapplication.viewmodel.ButtonUIState


class BrowseMenuView : AbstractAuthenticatedActivity() {

    private lateinit var adapter: ProductListViewAdapter
    private lateinit var binding: ActivityBrowseMenuViewBinding
    private lateinit var browseMenuViewModel: BrowseMenuViewModel
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseMenuViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        browseMenuViewModel = ViewModelProvider(this).get(BrowseMenuViewModel::class.java)
        adapter =  ProductListViewAdapter(this, browseMenuViewModel.productList.value!!)

        binding.listProducts.adapter = adapter

        // Add drawer layout
        drawerLayout = findViewById(R.id.browse_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        browseMenuViewModel.uiButtonState.observe(this) { state ->
            when (state) {
                ButtonUIState.VISIBLE -> binding.btnAddMenuItem.isVisible = true
                else -> binding.btnAddMenuItem.isVisible = false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        browseMenuViewModel.refreshProducts()
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun onAddMenuItemButtonClicked(view: View) {
        val productId = browseMenuViewModel.addProduct()

        if (productId == -1L) {
            // Error
            return
        }

        adapter.notifyDataSetChanged()
        Toast.makeText(this, "New menu item added", Toast.LENGTH_SHORT).show()
    }
}