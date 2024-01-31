package com.example

import com.example.models.DeliveryFeeRequest
import com.example.models.DeliveryFeeResponse
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class DeliveryFeeRequestsTest {
    @Test
    fun requestDeliveryWithCartValueBelowMin() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, 2235, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(710),
            """
            Delivery fee should be = 710:
            - cart value = 790, surcharge = 210,
            - distance = 2235, surcharge = 500,
            - number of items = 4, surcharge = 0,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryToNormalDistance() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 2235, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(500),
            """
            Delivery fee should be = 710:
            - cart value = 1000, surcharge = 0,
            - distance = 2235, surcharge = 500,
            - number of items = 4, surcharge = 0,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryToNoDistance() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 0, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(100),
            """
            Delivery fee should be = 100:
            - cart value = 1000, surcharge = 0,
            - distance = 2235, surcharge = 100,
            - number of items = 4, surcharge = 0,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryToLongDistance() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 22350, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(1500),
            """
            Delivery fee should be = 1500 (MAX):
            - cart value = 1000, surcharge = 0,
            - distance = 22350, surcharge = 45700,
            - number of items = 4, surcharge = 0,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryWith5Items() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 500, 5, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(150),
            """
            Delivery fee should be = 150:
            - cart value = 1000, surcharge = 0,
            - distance = 500, surcharge = 100,
            - number of items = 5, surcharge = 50,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryWith12Items() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 500, 12, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(500),
            """
            Delivery fee should be = 500:
            - cart value = 1000, surcharge = 0,
            - distance = 500, surcharge = 100,
            - number of items = 8, surcharge = 400,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryWithBulkItems() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(1000, 500, 14, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(720),
            """
            Delivery fee should be = 720:
            - cart value = 1000, surcharge = 0,
            - distance = 500, surcharge = 100,
            - number of items = 14, surcharge = 620,
            - order time = 13:00, surcharge = 0.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryOnFridayRushAt15() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, 2235, 4, "2024-01-19T15:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(852),
            """
            Delivery fee should be = 852:
            - cart value = 790, surcharge = 210,
            - distance = 2235, surcharge = 500,
            - number of items = 4, surcharge = 0,
            - order time = 15:00, surcharge = x1.2.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryOnFridayRushAt19() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, 2235, 4, "2024-01-19T19:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(852),
            """
            Delivery fee should be = 852:
            - cart value = 790, surcharge = 210,
            - distance = 2235, surcharge = 500,
            - number of items = 4, surcharge = 0,
            - order time = 19:00, surcharge = x1.2.
            """.trimIndent(),
        )

    @Test
    fun requestDeliveryWithCartValue200() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(20000, 2235, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.OK,
            DeliveryFeeResponse(0),
            "Delivery fee should be = 0, cart value is over 200€.",
        )

    @Test
    fun badRequestWithNegativeCartValue() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(-2, 2235, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.BadRequest,
        )

    @Test
    fun badRequestWithNegativeDistance() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, -2235, 4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.BadRequest,
        )

    @Test
    fun badRequestWithNegativeNumberOfItems() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, 2235, -4, "2024-01-15T13:00:00Z"),
            HttpStatusCode.BadRequest,
        )

    @Test
    fun badRequestWithWrongDateFormat() =
        testDeliveryFeeRequest(
            DeliveryFeeRequest(790, 2235, 4, "13:00:00"),
            HttpStatusCode.BadRequest,
        )

    fun testDeliveryFeeRequest(
        request: DeliveryFeeRequest,
        expectedStatus: HttpStatusCode,
        expectedResponse: DeliveryFeeResponse? = null,
        message: String = "",
    ) {
        testApplication {
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response =
                client.post("/delivery-fee") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            assertEquals(expectedStatus, response.status)

            if (!response.status.isSuccess()) return@testApplication

            val decodedResponse: DeliveryFeeResponse =
                Json.decodeFromString<DeliveryFeeResponse>(response.bodyAsText())
            assertEquals(expectedResponse, decodedResponse, message)
        }
    }
}
