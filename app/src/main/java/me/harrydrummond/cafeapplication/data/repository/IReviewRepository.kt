package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.CrudRepository

/**
 * Interface for performing CRUD operations on a review repository.
 */
interface IReviewRepository: CrudRepository<Review> {

    /**
     * Gets all reviews by their corresponding product id
     *
     * @param productId Id of the product to fetch reviews by
     * @return List of reviews from product.
     */
    fun getReviewsByProductId(productId: Int): List<Review>
}