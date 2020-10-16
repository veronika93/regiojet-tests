package sk.veronika.simonova.regiojet.gui.pages

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By

class HomePage {

    fun open() {
        Selenide.open("https://www.regiojet.sk/")
    }

    fun closeNotification() {
        Selenide.`$`(By.id("onesignal-slidedown-cancel-button"))
                .click()
    }

    fun setDepartureDate(departureDate: String) {
        Selenide.`$`(By.id("departure_date"))
                .setValue(departureDate)
                .waitUntil(Condition.value(departureDate), 5000, 500)
                .pressEnter()
    }

    fun clickOnSearchButton() {
        Selenide.`$`(By.className("ybus-form-submit"))
                .scrollTo()
                .waitUntil(Condition.visible, 5000, 500)
                .click()
    }

    fun setDestination(elementId: String, value: String, autoCompleteId: String) {
        Selenide.`$`(By.id(elementId))
                .setValue(value)
                .also {
                    waitUntilVisible(Selenide.`$`(By.id(autoCompleteId)))
                }
                .pressEnter()
    }

    fun waitUntilVisible(title: SelenideElement) {
        title.waitUntil(Condition.visible, 2000, 200)
    }
}