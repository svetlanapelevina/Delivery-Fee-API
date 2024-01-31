package com.example.plugins

import com.example.constants.*
import com.example.models.DeliveryFeeRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import java.text.ParseException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Application.configureDeliveryFeeRequestValidation() {
    install(RequestValidation) {
        validate<DeliveryFeeRequest> { request ->
            when {
                (request.cart_value < 0) -> return@validate ValidationResult.Invalid("Cart value can't be less than 0.")
                (request.number_of_items < 0) -> return@validate ValidationResult.Invalid("Number of items can't be less than 0.")
                (request.delivery_distance < 0) -> return@validate ValidationResult.Invalid("Delivery distance can't be less than 0.")
            }

            try {
                LocalDateTime.parse(request.time, DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN))
            } catch (cause: ParseException) {
                return@validate ValidationResult.Invalid("Time should be in ISO format. Example: 2024-01-15T13:00:00Z.")
            }

            return@validate ValidationResult.Valid
        }
    }
}
