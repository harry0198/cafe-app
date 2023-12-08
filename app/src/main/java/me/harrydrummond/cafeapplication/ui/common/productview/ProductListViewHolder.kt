package me.harrydrummond.cafeapplication.ui.common.productview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.databinding.ActivityProductListViewBinding
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.logic.toBitmap
import me.harrydrummond.cafeapplication.logic.toPrice

/**
 * List adapter for viewing ProductModels with a next button action.
 *
 * @param context Context object
 * @param productList List of ProductModels to display
 * @param onItemClick Callback for when an item in clicked
 *
 * @see ProductListView
 */
class ProductListViewAdapter(context: Context, var productList: List<Product>, val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<ProductListViewAdapter.ProductListViewHolder>() {

    private val colours = listOf(R.color.pastel_green,)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var binding: ActivityProductListViewBinding

    inner class ProductListViewHolder(itemBinding: ActivityProductListViewBinding): RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListViewHolder {
        binding = ActivityProductListViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val product = productList[position]

        holder.itemView.apply {
            binding.productName.text = product.productName
            binding.productPrice.text = product.productPrice.toPrice()
            binding.productImage.setImageBitmap(product.productImage?.toBitmap(400, 400))
            binding.root.setOnClickListener {
                onItemClick(product)
            }

            // Make colours alternate
            val colour = colours[position % colours.size]
            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    colour
                )
            )

        }
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}