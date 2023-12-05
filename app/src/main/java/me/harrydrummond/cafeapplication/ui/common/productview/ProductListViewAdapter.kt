package me.harrydrummond.cafeapplication.ui.common.productview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.harrydrummond.cafeapplication.databinding.ActivityProductListViewBinding
import me.harrydrummond.cafeapplication.data.model.Product

/**
 * List adapter for viewing ProductModels with a next button action.
 *
 * @param context Context object
 * @param productList List of ProductModels to display
 * @param onItemClick Callback for when an item in clicked
 *
 * @see ProductListView
 */
class ProductListViewAdapter(context: Context, var productList: List<Product>, val onItemClick: (Product) -> Unit) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(p0: Int): Product {
        return productList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        // Set up binding, store it as a tag.
        val binding: ActivityProductListViewBinding
        if (myView == null) {
            binding = ActivityProductListViewBinding.inflate(inflater, parent, false)
            myView = binding.root
            myView.setTag(binding)
        } else {
            binding = myView.tag as ActivityProductListViewBinding
        }

        // Update fields
        val product = getItem(position)

        binding.productName.text = product.productName
        binding.productPrice.text = "£ " + String.format("%.2f", product.productPrice)
        binding.nextButton.setOnClickListener {
            onItemClick(product)
        }

        return myView
    }
}