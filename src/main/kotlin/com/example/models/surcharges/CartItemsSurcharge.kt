package com.example.models.surcharges

const val SURCHARGE_PER_ITEM_OVER_MIN: Double = 50.0
const val SURCHARGE_FOR_BULK_ITEMS: Double = 120.0
const val MIN_ITEMS_WITHOUT_SURCHARGE: Int = 4
const val MIN_ITEMS_WITHOUT_BULK_SURCHARGE: Int = 12

class CartItemsSurcharge(private val numberOfItems: Int) : Surcharge {
    override val amount: Double =
        if (this.numberOfItems <= MIN_ITEMS_WITHOUT_SURCHARGE) {
            0.0
        } else {
            (this.numberOfItems - MIN_ITEMS_WITHOUT_SURCHARGE) * SURCHARGE_PER_ITEM_OVER_MIN +
                if (this.numberOfItems <= MIN_ITEMS_WITHOUT_BULK_SURCHARGE) 0.0 else SURCHARGE_FOR_BULK_ITEMS
        }
}
