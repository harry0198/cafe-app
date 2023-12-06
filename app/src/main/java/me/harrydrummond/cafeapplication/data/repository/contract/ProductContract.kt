package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.toProduct
import me.harrydrummond.cafeapplication.logic.toByteArray

/**
 * Contains the table name and columns for creating the sqlite database.
 * Also contains the create statement.
 */
object ProductContract : BaseContract<Product> {
    const val NAME = "ProdName"
    const val PRICE = "ProdPrice"
    const val IMAGE = "ProdImage"
    const val DESCRIPTION = "ProdDescription"
    const val AVAILABLE = "ProdAvailable"

    override val TABLE_NAME = "Product"
    override val ID = "ProdId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, " +
            "$PRICE NUMERIC, $IMAGE BLOB, $DESCRIPTION TEXT, $AVAILABLE BOOLEAN NOT NULL DEFAULT 1 )"

    /**
     * @inheritDoc
     */
    override fun toEntity(cursor: Cursor): Product = cursor.toProduct()

    /**
     * @inheritDoc
     */
    override fun getId(entity: Product): Int = entity.productId

    /**
     * @inheritDoc
     */
    override fun getEntityValues(entity: Product, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.productId)
        cv.put(NAME, entity.productName)
        cv.put(DESCRIPTION, entity.productDescription)
        cv.put(IMAGE, entity.productImage)
        cv.put(PRICE, entity.productPrice)
        cv.put(AVAILABLE, entity.productAvailable)

        return cv
    }
}