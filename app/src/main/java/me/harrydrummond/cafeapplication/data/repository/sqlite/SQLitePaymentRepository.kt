package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.Payment
import me.harrydrummond.cafeapplication.data.repository.IPaymentRepository
import me.harrydrummond.cafeapplication.data.repository.contract.PaymentContract

class SQLitePaymentRepository(helper: DataBaseHelper): AbstractSQLiteRepository<Payment>(helper, PaymentContract), IPaymentRepository {
}