package me.harrydrummond.cafeapplication.ui.common.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.harrydrummond.cafeapplication.R

class OrderListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list_view)
    }
}