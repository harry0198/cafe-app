package me.harrydrummond.cafeapplication.data.repository.sqlite

import me.harrydrummond.cafeapplication.data.model.Order
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.contract.OrderContract

class SQLiteOrderRepository(helper: DataBaseHelper): AbstractSQLiteRepository<Order>(helper, OrderContract),
    IOrderRepository