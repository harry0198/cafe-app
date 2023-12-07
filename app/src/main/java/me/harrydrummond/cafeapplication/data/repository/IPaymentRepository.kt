package me.harrydrummond.cafeapplication.data.repository

import me.harrydrummond.cafeapplication.data.model.Payment

/**
 * Interface for performing CRUD operations on a payment repository.
 */
interface IPaymentRepository: CrudRepository<Payment> {
}