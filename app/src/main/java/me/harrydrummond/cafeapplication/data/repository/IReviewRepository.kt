package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Review
import me.harrydrummond.cafeapplication.data.repository.CrudRepository

interface IReviewRepository: CrudRepository<Review> {

    fun getReviewsByProductId(productId: Int): List<Review>
}