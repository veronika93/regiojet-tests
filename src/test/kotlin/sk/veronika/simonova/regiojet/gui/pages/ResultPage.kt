package sk.veronika.simonova.regiojet.gui.pages

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By

class ResultPage {

    fun getTitle():SelenideElement{
        return Selenide.`$`(By.xpath("//div[contains(@class, 'header')]/h3"))
    }

    fun getDepartureDate(): SelenideElement {
      return  Selenide.`$`(By.xpath("//div[contains(@class, 'text-h2')]/span[contains(@class, 'date')]"))
    }
}