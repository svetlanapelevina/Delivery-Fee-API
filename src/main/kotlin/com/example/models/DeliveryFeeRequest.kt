package com.example.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
data class DeliveryFeeRequest(val cart_value: Int, val delivery_distance: Int, val number_of_items: Int, val time: String)