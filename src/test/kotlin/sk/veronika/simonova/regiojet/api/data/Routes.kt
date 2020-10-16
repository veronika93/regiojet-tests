package sk.veronika.simonova.regiojet.api.data

import java.time.OffsetDateTime

data class Routes(
        val routes: List<Route>,
        val routesMessage: String?,
        val bannerBubbles: List<String>?,
        val textBubbles: List<String>?)

data class Route(
        val id: String,
        val departureStationId: Long?,
        val departureTime: OffsetDateTime,
        val arrivalStationId: Long?,
        val arrivalTime: OffsetDateTime,
        val vehicleTypes: List<String>?,
        val transfersCount: Int?,
        val freeSeatsCount: Int?,
        val priceFrom: Double,
        val priceTo: Double?,
        val creditPriceFrom: Double?,
        val creditPriceTo: Double?,
        val pricesCount: Int?,
        val actionPrice: Boolean?,
        val surcharge: Boolean?,
        val notices: Boolean?,
        val support: Boolean?,
        val nationalTrip: Boolean?,
        val bookable: Boolean?,
        val delay: String?,
        val travelTime: String?,
        val vehicleStandardKey: String?
)