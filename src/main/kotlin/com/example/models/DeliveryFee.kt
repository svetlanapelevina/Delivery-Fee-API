package com.example.models

import com.example.models.surcharges.*
import com.example.constants.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val MAX_DELIVERY_FEE: Double = 1500.0
private const val MIN_DELIVERY_FEE: Double = 100.0
private const val MIN_CART_VALUE_WITHOUT_FEE: Double = 20000.0

class DeliveryFee(private val cartValue: Int, private val numberOfItems: Int, private val distance: Int, private val timeISO: String) {
    private val orderDateTime = LocalDateTime.parse(this.timeISO, DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN))

    private val feeApplied: Boolean = (this.cartValue < MIN_CART_VALUE_WITHOUT_FEE)

    private val surcharges: Double =  listOf(
            CartValueSurcharge(this.cartValue),
            CartItemsSurcharge(this.numberOfItems),
            DistanceSurcharge(this.distance)).sumOf{it.amount}

    private val coefficients: Double = OrderTimeSurcharge(this.orderDateTime).amount

    var amount: Double = if (this.feeApplied) (this.surcharges * this.coefficients).coerceIn(MIN_DELIVERY_FEE, MAX_DELIVERY_FEE) else 0.0
}