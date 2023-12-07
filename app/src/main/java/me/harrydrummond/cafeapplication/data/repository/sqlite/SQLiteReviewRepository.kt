package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IReviewRepository
import me.harrydrummond.cafeapplication.data.repository.contract.ReviewContract

/**
 * An SQLite repository for reviews.
 *
 * @param helper [DataBaseHelper] to run operations on.
 */
class SQLiteReviewRepository(helper: DataBaseHelper): AbstractSQLiteRepository<Review>(helper, ReviewContract),
    IReviewRepository {

    /**
     * @inheritDoc
     */
    override fun getReviewsByProductId(productId: Int): List<Review> {
        val query = "${ReviewContract.PRODUCT_ID} = ?"
        return getAllByQuery(query, productId.toString())
    }


}