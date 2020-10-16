package sk.veronika.simonova.regiojet.gui.tests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import sk.veronika.simonova.regiojet.gui.pages.Pages

class ConnectionTests {
    private val departureDate = "19.10.2020"
    private val homePage = Pages.homePage
    private val resultPage = Pages.resultPage

    @Test
    fun `find direct connections between Ostrava and Brno and validate response`() {
        findConnections()
        val title = resultPage.getTitle()
        homePage.waitUntilVisible(title)
        assertThat(title.text()).contains("Ostrava")
        assertThat(title.text()).contains("Brno")
        assertThat(resultPage.getDepartureDate().text().replace(Regex("\\s"),"")).isEqualTo("Pondelok$departureDate")
    }


    private fun findConnections() {
        homePage.open()
        homePage.closeNotification()
        homePage.setDestination("destination_from", "Ostrava", "ui-id-5")
        homePage.setDestination("destination_to", "Brno", "ui-id-6")
        homePage.setDepartureDate(departureDate)
        homePage.clickOnSearchButton()

    }


}
