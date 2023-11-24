package me.harrydrummond.cafeapplication.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import me.harrydrummond.cafeapplication.data.DatabaseHelper
import me.harrydrummond.cafeapplication.model.ProductModel
import me.harrydrummond.cafeapplication.model.UserModel

class ProductRepository(context: Context) : Repository {

    private val dbHelper = DatabaseHelper(context)
    companion object {
        const val TABLE_NAME = "product"
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val PRODUCT_PRICE = "product_price"
        const val PRODUCT_IMAGE = "product_image"
        const val PRODUCT_DESCRIPTION = "product_description"
        const val PRODUCT_AVAILABLE = "product_available"
    }

    override fun create(sql: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME " +
                "($PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PRODUCT_NAME TEXT, " +
                "$PRODUCT_PRICE NUMERIC, " +
                "$PRODUCT_DESCRIPTION TEXT, " +
                "$PRODUCT_IMAGE TEXT, " +
                "$PRODUCT_AVAILABLE BOOLEAN)"

        sql.execSQL(createTableStatement)
    }

    override fun update(sql: SQLiteDatabase) {
        // nothing
    }

    fun addProduct(product: ProductModel): Long {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(PRODUCT_NAME, product.productName)
            put(PRODUCT_DESCRIPTION, product.productDescription)
            put(PRODUCT_PRICE, product.productPrice)
            put(PRODUCT_IMAGE, product.productImage)
            put(PRODUCT_AVAILABLE, product.productAvailable)
        }

        try {
            return db.insert(TABLE_NAME, null, values)
        } finally {
            db.close()
        }
    }

    fun getProducts(): MutableList<ProductModel> {

        val productList: MutableList<ProductModel> = ArrayList()
        val db = dbHelper.writableDatabase

        val cursor = db.query(
            TABLE_NAME,  // Table name
            arrayOf("*"),  // Columns to retrieve
            null,  // WHERE clause
            null,  // Arguments for WHERE clause
            null,  // GROUP BY (if any)
            null,  // HAVING (if any)
            null // ORDER BY (if any)
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Assuming Product is a class with appropriate constructor
                val product = getProductModelFromCursor(cursor)
                productList.add(product)
            } while (cursor.moveToNext())
            cursor.close()
        }

        db.close()
        return productList
    }

    fun getProductById(id: Long): ProductModel? {
            val db = dbHelper.readableDatabase
            val selection = "$PRODUCT_ID = ?"
            val selectionArgs = arrayOf(id.toString())
            var cursor: Cursor? = null

            try {
                cursor = db.query(
                    TABLE_NAME,  // Table name
                    arrayOf("*"),  // Columns to retrieve
                    selection,  // WHERE clause
                    selectionArgs,  // Arguments for WHERE clause
                    null,  // GROUP BY
                    null,  // HAVING
                    null // ORDER BY
                )

                val product: ProductModel?

                if (cursor != null && cursor.moveToFirst()) {
                    product = getProductModelFromCursor(cursor)
                    return product
                }
            } finally {
                cursor?.close()
                db.close()
            }

            return null
    }

    @SuppressLint("Range")
    private fun getProductModelFromCursor(cursor: Cursor): ProductModel {
        return ProductModel(
            cursor.getLong(cursor.getColumnIndex(PRODUCT_ID)),
            cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)),
            cursor.getDouble(cursor.getColumnIndex(PRODUCT_PRICE)),
            cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE)),
            cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)),
            cursor.getInt(cursor.getColumnIndex(PRODUCT_AVAILABLE)) == 1
        )
    }

}