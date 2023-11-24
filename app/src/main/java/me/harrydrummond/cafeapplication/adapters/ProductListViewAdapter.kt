package me.harrydrummond.cafeapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.view.ProductViewActivity
import me.harrydrummond.cafeapplication.model.ProductModel

class ProductListViewAdapter(private val context: Context, private val productList: MutableList<ProductModel>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(p0: Int): ProductModel {
        return productList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        if (myView == null) {
            myView = inflater.inflate(R.layout.activity_product_list_view, parent, false)

            val product = getItem(position)


            val productName = myView.findViewById<TextView>(R.id.productName)
            val productPrice = myView.findViewById<TextView>(R.id.productPrice)
            val productViewButton = myView.findViewById<Button>(R.id.nextButton)

            val price = "£ ${product.productPrice}"

            productName.text = product.productName
            productPrice.text = price

            productViewButton.setOnClickListener {
                val intent = Intent(context, ProductViewActivity::class.java).apply {
                    putExtra("PRODUCT", product)
                }

                context.startActivity(intent)
            }
        }

        return myView
    }
}