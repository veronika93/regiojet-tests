package sk.veronika.simonova.regiojet.api.tests

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import sk.veronika.simonova.regiojet.api.RouteTravelTimeComparator
import sk.veronika.simonova.regiojet.api.data.*
import java.util.stream.Stream

const val BASE_URL = "https://brn-ybus-pubapi.sa.cz/restapi/"

class ConnectionTests {

    private val departureYear = 2020
    private val departureMonth = 10
    private val departureDay = 19
    private val placeOfDeparture = "Ostrava"
    private val placeOfArrival = "Brno"

    @ParameterizedTest(name = "Test for {0} passenger/s with tariff/s: {1}")
    @MethodSource("getPassengers")
    fun `find direct connections between Ostrava and Brno and validate response`(passengers: Int, tariffs: Array<out String>) {
        val country = getCzCountry()
        val departure = getCity(country, placeOfDeparture)
        val arrival = getCity(country, placeOfArrival)
        val departureStationIds = departure.stations.map { station -> station.id }
        val arrivalStationIds = arrival.stations.map { station -> station.id }

        val directRoutes = getDirectConnections(departure, arrival, tariffs)

        directRoutes.forEach {
            checkDepartureDate(it)
            assertThat(it.departureStationId).isIn(departureStationIds)
            assertThat(it.arrivalStationId).isIn(arrivalStationIds)
            assertThat(it.transfersCount).isEqualTo(0)
        }
    }

    @ParameterizedTest(name = "Test for {0} passenger/s with tariff/s: {1}")
    @MethodSource("getPassengers")
    fun `find optimal connections between Ostrava and Brno with faster arrival time`(passengers: Int, tariffs: Array<out String>) {
        val country = getCzCountry()
        val departure = getCity(country, placeOfDeparture)
        val arrival = getCity(country, placeOfArrival)

        val directRoutes = getDirectConnections(departure, arrival, tariffs)
        assertThat(directRoutes.minBy { route -> route.arrivalTime }).isNotNull()
    }

    @ParameterizedTest(name = "Test for {0} passenger/s with tariff/s: {1}")
    @MethodSource("getPassengers")
    fun `find optimal connections between Ostrava and Brno with shortest time spent with travelling`(passengers: Int, tariffs: Array<out String>) {
        val country = getCzCountry()
        val departure = getCity(country, placeOfDeparture)
        val arrival = getCity(country, placeOfArrival)

        val directRoutes = getDirectConnections(departure, arrival, tariffs)
        assertThat(directRoutes.sortedWith(RouteTravelTimeComparator()).first()).isNotNull()
    }

    @ParameterizedTest(name = "Test for {0} passenger/s with tariff/s: {1}")
    @MethodSource("getPassengers")
    fun `find optimal connections between Ostrava and Brno with lowest price of journey`(passengers: Int, tariffs: Array<out String>) {
        val country = getCzCountry()
        val departure = getCity(country, placeOfDeparture)
        val arrival = getCity(country, placeOfArrival)

        val directRoutes = getDirectConnections(departure, arrival, tariffs)
        assertThat(directRoutes.minBy { route -> route.priceFrom }).isNotNull()
    }

    private fun getDirectConnections(departure: City, arrival: City, tariffs: Array<out String>): List<Route> {
        val routes = getRoutesWithParameters(
                mutableMapOf(
                        "departureDate" to "$departureYear-$departureMonth-$departureDay",
                        "fromLocationId" to departure.id.toString(),
                        "fromLocationType" to "CITY",
                        "toLocationId" to arrival.id.toString(),
                        "toLocationType" to "CITY"),
                *tariffs)

        assertThat(routes.routes).isNotEmpty
        return routes.routes.filter { r -> r.transfersCount == 0 }
    }

    private fun checkDepartureDate(it: Route) {
        assertThat(it.departureTime.year).isEqualTo(departureYear)
        assertThat(it.departureTime.monthValue).isEqualTo(departureMonth)
        assertThat(it.departureTime.dayOfMonth).isEqualTo(departureDay)
    }

    private fun getCzCountry(): Country {
        return getLocations().first { country -> country.code == "CZ" }
    }

    private fun getCity(country: Country, cityName: String): City {
        return country.cities.first { city -> city.name == cityName }
    }

    private fun getRoutesWithParameters(params: Map<String, Any>, vararg tariffs: String): Routes {
        return given()
                .queryParams(params)
                .apply {
                    tariffs.forEach {
                        queryParam("tariffs", it)
                    }
                }
                .`when`()
                .get("routes/search/simple")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response()
                .`as`(Routes::class.java)
    }

    private fun getLocations(): List<Country> {
        return given()
                .`when`()
                .get("consts/locations")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response()
                .`as`(Array<Country>::class.java)
                .asList()
    }

    private fun given(): RequestSpecification {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .relaxedHTTPSValidation()
                .log().ifValidationFails()
    }

    companion object {
        @JvmStatic
        fun getPassengers(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(1, arrayOf("REGULAR")),
                    Arguments.of(2, arrayOf("REGULAR", "REGULAR")),
                    Arguments.of(3, arrayOf("REGULAR", "REGULAR", "REGULAR")),
                    Arguments.of(4, arrayOf("REGULAR", "REGULAR", "REGULAR", "REGULAR")),
                    Arguments.of(5, arrayOf("REGULAR", "REGULAR", "REGULAR", "REGULAR", "REGULAR")),
                    Arguments.of(6, arrayOf("REGULAR", "REGULAR", "REGULAR", "REGULAR", "REGULAR", "REGULAR")),

                    Arguments.of(1, arrayOf("ISIC")),
                    Arguments.of(2, arrayOf("REGULAR", "ISIC")),
                    Arguments.of(3, arrayOf("ISIC", "REGULAR", "ISIC")),
                    Arguments.of(4, arrayOf("REGULAR", "ISIC", "REGULAR", "ISIC")),
                    Arguments.of(5, arrayOf("ISIC", "REGULAR", "ISIC", "REGULAR", "ISIC")),
                    Arguments.of(6, arrayOf("REGULAR", "ISIC", "REGULAR", "REGULAR", "REGULAR", "REGULAR")))
        }
    }
}
