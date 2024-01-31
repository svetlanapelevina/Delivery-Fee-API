package com.example.routes

import com.example.models.DeliveryFee
import com.example.models.DeliveryFeeRequest
import com.example.models.DeliveryFeeResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deliveryFeeRoute() {
    route("/delivery-fee") {
        post {
            try {
                val request = call.receive<DeliveryFeeRequest>()
                val deliveryFeeInCents: Int =
                    DeliveryFee(
                        request.cart_value,
                        request.number_of_items,
                        request.delivery_distance,
                        request.time,
                    ).amount.toInt()
                call.respond(DeliveryFeeResponse(deliveryFeeInCents))
            } catch (exception: Exception) {
                call.respond(HttpStatusCode.BadRequest, exception.message.toString())
            }
        }
    }
}
