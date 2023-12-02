package me.harrydrummond.cafeapplication.ui.common.reviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.harrydrummond.cafeapplication.R

class ReviewListView  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list_view)
    }
}