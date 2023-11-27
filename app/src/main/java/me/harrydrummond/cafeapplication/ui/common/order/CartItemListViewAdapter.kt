package me.harrydrummond.cafeapplication.ui.common.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.data.model.ProductModel
import me.harrydrummond.cafeapplication.databinding.OrderDetailsListViewBinding
import java.util.Dictionary

class CartItemListViewAdapter (context: Context, var cartItems: List<Pair<Int, ProductModel>>, val onEditClick: ((ProductModel) -> Unit)?, val onDeleteClick: ((ProductModel) -> Unit)?) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return cartItems.size
    }

    override fun getItem(p0: Int): Pair<Int, ProductModel> {
        return cartItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        // Set up binding, store it as a tag.
        val binding: OrderDetailsListViewBinding
        if (myView == null) {
            binding = OrderDetailsListViewBinding.inflate(inflater, parent, false)
            myView = binding.root
            myView.setTag(binding)
        } else {
            binding = myView.tag as OrderDetailsListViewBinding
        }

        val prodPair = getItem(position)
        val quantity = prodPair.first
        val product = prodPair.second

        val totalPrice = product.productPrice * quantity

        binding.lblTitle.text = "x$quantity ${product.productName}"
        binding.lblSubTotal.text = "£$totalPrice"
        binding.lblDesc.text = product.productDescription

        if (onEditClick != null) {
            binding.btnEditCartItem.setOnClickListener {
                onEditClick.invoke(product)
            }
        } else {
            binding.btnEditCartItem.isVisible = false
        }

        if (onDeleteClick != null) {
            binding.btnDeleteFromCart.setOnClickListener {
                onDeleteClick.invoke(product)
            }
        } else {
            binding.btnDeleteFromCart.isVisible = false
        }

        return myView
    }
}