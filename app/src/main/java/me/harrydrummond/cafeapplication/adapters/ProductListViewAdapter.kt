package me.harrydrummond.cafeapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import me.harrydrummond.cafeapplication.R

class ProductListViewAdapter(context: Context, private val productList: Array<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(p0: Int): String {
        return productList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        if (myView == null) {
            myView = inflater.inflate(R.layout.activity_product_list_view, parent, false)
            val productName = myView.findViewById<TextView>(R.id.txtProductName)

            productName.text = getItem(position)
        }

        return myView
    }
}