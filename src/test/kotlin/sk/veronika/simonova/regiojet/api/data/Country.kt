package sk.veronika.simonova.regiojet.api.data

import java.math.BigDecimal

data class Country(
        val country: String?,
        val code: String?,
        val cities: List<City>
)

data class City(
        val id: Long?,
        val name: String?,
        val aliases: List<String>?,
        val stationsTypes: List<String>?,
        val stations: List<Station>
)

data class Station(
        val id: Long,
        val name: String?,
        val fullname: String?,
        val aliases: List<String>?,
        val address: String?,
        val stationsTypes: List<String>?,
        val iataCode: String?,
        val stationUrl: String?,
        val stationTimeZoneCode: String?,
        val wheelChairPlatform: String?,
        val significance: Int?,
        val longitude: BigDecimal?,
        val latitude: BigDecimal?,
        val imageUrl: String?
)




