package com.assignment.currency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat

@Entity
data class CurrencyRateEntity(
    @PrimaryKey
    val code: String,
    var rate: Double,
    val baseRate: Double = 1.0,
    val isMain: Boolean = false
) {

    fun recalculateRate(multiplier: Double) {
        rate = recalculateRate(multiplier, baseRate)
    }

    fun recalculateRate(multiplier: Double, baseRate: Double) : Double {
        val numberFormat = DecimalFormat("#.00")
        return numberFormat.format(baseRate * multiplier).toDouble()
    }
}
