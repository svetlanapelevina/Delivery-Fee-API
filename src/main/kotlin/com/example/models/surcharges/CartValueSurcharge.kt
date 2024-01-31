package com.example.models.surcharges

const val MIN_CART_VALUE_WITHOUT_SURCHARGE: Double = 1000.0

class CartValueSurcharge(private val cartValue: Int) : Surcharge {
    override val amount: Double = if (this.cartValue > MIN_CART_VALUE_WITHOUT_SURCHARGE) 0.0 else MIN_CART_VALUE_WITHOUT_SURCHARGE - this.cartValue
}