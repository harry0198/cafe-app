package me.harrydrummond.cafeapplication.ui.common.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.databinding.OrderDetailsListViewBinding
import me.harrydrummond.cafeapplication.logic.toPrice

/**
 * List view adapter for viewing a fully loaded cart.
 *
 * @param context Context object
 * @param cartItems Items to display in list view
 * @param onEditClick Callback for when the edit button is clicked. Null removes the display of edit btn.
 * @param onDeleteClick Callback for when the delete button i clicked. Null removed the display of delete btn.
 *
 * @see CartItemListView
 */
class CartItemListViewAdapter (context: Context, var cartItems: List<Pair<Int, Product>>, private val onEditClick: ((Product) -> Unit)?, private val onDeleteClick: ((Product) -> Unit)?) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return cartItems.size
    }

    override fun getItem(p0: Int): Pair<Int, Product> {
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

        // Setup view values.
        val prodPair = getItem(position)
        val quantity = prodPair.first
        val product = prodPair.second

        val totalPrice = product.productPrice * quantity

        binding.lblTitle.text = "x$quantity ${product.productName}"
        binding.lblSubTotal.text = totalPrice.toPrice()
        binding.lblDesc.text = product.productDescription

        // If edit btn callback is null, don't display the edit btn
        if (onEditClick != null) {
            binding.btnEditCartItem.setOnClickListener {
                onEditClick.invoke(product)
            }
        } else {
            binding.btnEditCartItem.isVisible = false
        }

        // If the delete btn callback is null, don't display the delete btn.
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