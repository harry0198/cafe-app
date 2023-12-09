package me.harrydrummond.cafeapplication.ui.common.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.databinding.ActivityOrderListViewBinding

/**
 * List view adapter for viewing orders.
 *
 * @param context Context object
 * @param orderItems List of orders to display in list
 * @param onNextClick Callback for when the next button is clicked
 *
 * @see OrderListView
 */
class OrderListViewAdapter (context: Context, var orderItems: List<Order>, val onNextClick: (Order) -> Unit) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return orderItems.size
    }

    override fun getItem(p0: Int): Order {
        return orderItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        // Set up binding, store it as a tag.
        // This was from a youtube video on how to setup binding in listviews.
        val binding: ActivityOrderListViewBinding
        if (myView == null) {
            binding = ActivityOrderListViewBinding.inflate(inflater, parent, false)
            myView = binding.root
            myView.setTag(binding)
        } else {
            binding = myView.tag as ActivityOrderListViewBinding
        }

        // Update the view
        val order = getItem(position)
        val total = order.products.size

        binding.lblTitle.text = "Order ID: ${order.orderId}"
        binding.lblItemCount.text = "x$total"
        binding.status.text = order.status.toString()

        binding.nextButton.setOnClickListener {
            onNextClick(order)
        }

        return myView
    }
}