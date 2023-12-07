package me.harrydrummond.cafeapplication.mocks

import me.harrydrummond.cafeapplication.data.model.Payment
import me.harrydrummond.cafeapplication.data.repository.IPaymentRepository

class FakePaymentRepository: IPaymentRepository {
    private val payments: MutableList<Payment> = mutableListOf()

    /**
     * @inheritDoc
     */
    override fun save(type: Payment): Int {
        val id = (1..100).random()
        val payment = type.copy(paymentId = id)
        payments.add(payment)
        return id
    }

    /**
     * @inheritDoc
     */
    override fun update(type: Payment): Boolean {
        val payment = getById(type.paymentId)?: return false
        payments.remove(payment)
        payments.add(payment)
        return true
    }

    /**
     * @inheritDoc
     */
    override fun getById(id: Int): Payment? {
        return payments.find { it.paymentId == id }
    }

    /**
     * @inheritDoc
     */
    override fun delete(type: Payment): Boolean {
        return payments.remove(type)
    }
}