package com.example.models.surcharges

const val SURCHARGE_PER_DISTANCE_SECTION: Double = 100.0
const val DISTANCE_SECTION_IN_METERS: Int = 500

class DistanceSurcharge(private val distanceInMeters: Int) : Surcharge {
    private val numberOfSectorsInDistance: Int =
        this.distanceInMeters / DISTANCE_SECTION_IN_METERS + if (this.distanceInMeters % DISTANCE_SECTION_IN_METERS != 0) 1 else 0

    override val amount: Double = this.numberOfSectorsInDistance * SURCHARGE_PER_DISTANCE_SECTION
}
