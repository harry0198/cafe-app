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
        val query = "${ProductContract.AVAILABLE} = ?"
        return getAllByQuery(query, "1")
    }

    /**
     * @inheritDoc
     */
    override fun getAllProducts(): List<Product> {
        return getAllByQuery(null, null)
    }
}