package me.harrydrummond.cafeapplication.logic

import java.text.NumberFormat
import java.util.Locale


fun Double.toPrice(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.UK)
    return formatter.format(this)
}