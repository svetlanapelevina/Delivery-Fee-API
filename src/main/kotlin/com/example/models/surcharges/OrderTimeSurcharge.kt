package com.example.models.surcharges

import java.time.DayOfWeek
import java.time.LocalDateTime

const val SURCHARGE_COEFFICIENT_FOR_RUSH: Double = 1.2
const val NO_SURCHARGE_COEFFICIENT: Double = 1.0
const val RUSH_HOUR_START: Int = 15
const val RUSH_HOUR_END: Int = 19

class OrderTimeSurcharge(private val orderTime: LocalDateTime) : Surcharge {
    private fun isFridayRush(): Boolean =
        this.orderTime.dayOfWeek == DayOfWeek.FRIDAY && this.orderTime.hour in (RUSH_HOUR_START..RUSH_HOUR_END)

    override val amount: Double = if (this.isFridayRush()) SURCHARGE_COEFFICIENT_FOR_RUSH else NO_SURCHARGE_COEFFICIENT
}