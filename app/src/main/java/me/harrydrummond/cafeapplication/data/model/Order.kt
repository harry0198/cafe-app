package me.harrydrummond.cafeapplication.data.model

data class Order(var orderId: String = "", val products: List<ProductQuantity> = listOf(), var status: Status = Status.NONE, var userId: String = "")
