package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.ContentValues
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.contract.ProductContract

/**
 * An SQLite repository for products.
 *
 * @see AbstractSQLiteUserRepository
 * @see Product
 * @see IProductRepository
 */
class SQLiteProductRepository(helper: DataBaseHelper): AbstractSQLiteRepository<Product>(helper, ProductContract),
    IProductRepository {

    /**
     * @inheritDoc
     */
    override fun getAllAvailableProducts(): List<Product> {
        val query = "SELECT * FROM ${ProductContract.TABLE_NAME} WHERE ${ProductContract.AVAILABLE} = 1"
        return getAllByQuery(query, ProductContract)
    }

    /**
     * @inheritDoc
     */
    override fun getAllProducts(): List<Product> {
        val query = "SELECT * FROM ${ProductContract.TABLE_NAME}"
        return getAllByQuery(query, ProductContract)
    }
}