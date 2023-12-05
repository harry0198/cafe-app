package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.IReviewRepository
import me.harrydrummond.cafeapplication.data.repository.contract.ReviewContract


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