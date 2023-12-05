package me.harrydrummond.cafeapplication.ui.common.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.databinding.ActivityReviewListViewBinding


/**
 * List adapter for viewing reviews.
 *
 * @param context Context object
 * @param productList List of ProductModels to display
 *
 * @see ReviewListView
 */
class ReviewListViewAdapter(context: Context, var reviewList: List<Review>): BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return reviewList.size
    }

    override fun getItem(p0: Int): Review {
        return reviewList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var myView: View? = convertView

        // Set up binding, store it as a tag.
        val binding: ActivityReviewListViewBinding
        if (myView == null) {
            binding = ActivityReviewListViewBinding.inflate(inflater, parent, false)
            myView = binding.root
            myView.setTag(binding)
        } else {
            binding = myView.tag as ActivityReviewListViewBinding
        }

        val review = reviewList.get(position)

        binding.lblNumber.text = "#$position"
        binding.lblUserName.text = "TODO"
        binding.lblReview.text = review.review

        return myView
    }
}