package me.harrydrummond.cafeapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.model.ProductModel

class ProductViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)

        val product: ProductModel = intent.getSerializableExtra("PRODUCT") as ProductModel

        val productName = findViewById<TextView>(R.id.lblProductName)
        val productDesc = findViewById<TextView>(R.id.lblProductDescription)
        val productPrice = findViewById<TextView>(R.id.lblProductPrice)

        val price = "£${product.productPrice}"

        productName.text = product.productName
        productDesc.text = product.productDescription
        productPrice.text = price
    }
}