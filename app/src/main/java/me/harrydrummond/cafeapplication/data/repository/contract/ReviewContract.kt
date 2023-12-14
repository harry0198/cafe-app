package me.harrydrummond.cafeapplication.data.repository.contract

import android.content.ContentValues
import android.database.Cursor
import me.harrydrummond.cafeapplication.data.model.Product
import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.toProduct
import me.harrydrummond.cafeapplication.data.repository.toReview

/**
* Contains the table name and columns for creating the sqlite database.
* Also contains the create statement.
*/
object ReviewContract : BaseContract<Review> {
    const val USER_ID = "UserId"
    const val PRODUCT_ID = "ProductId"
    const val REVIEW = "Review"
    const val RATING = "Rating"

    override val TABLE_NAME = "Review"
    override val ID = "ReviewId"
    override val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID INTEGER NOT NULL, $PRODUCT_ID INTEGER NOT NULL, $RATING NUMERIC NOT NULL, $REVIEW TEXT NOT NULL )"

    /**
     * @inheritDoc
     */
    override fun toEntity(cursor: Cursor): Review = cursor.toReview()

    /**
     * @inheritDoc
     */
    override fun getId(entity: Review): Int = entity.productId

    /**
     * @inheritDoc
     */
    override fun getEntityValues(entity: Review, withPrimaryKey: Boolean): ContentValues {
        val cv = ContentValues()

        if (withPrimaryKey)
            cv.put(ID, entity.reviewId)
        cv.put(PRODUCT_ID, entity.productId)
        cv.put(USER_ID, entity.userId)
        cv.put(REVIEW, entity.review)
        cv.put(RATING, entity.rating)

        return cv
    }
}