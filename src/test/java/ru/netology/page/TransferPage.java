package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id = amount] input");
    private SelenideElement from = $("[data-test-id = from] input");
    private SelenideElement transferButton = $("[data-test-id = action-transfer");
    private SelenideElement cancelButton = $("[data-test-id = action-cancel");
    private SelenideElement errorNotification = $("[data-test-id = error-notification");

    public TransferPage() { //проверка на то, что страница открылась
        amount.shouldBe(Condition.visible);
    }

    public void transfer(String sum, DataHelper.CardInfo cardInfo) { //заполнение полей
        amount.setValue(sum);
        from.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public DashboardPage validTransfer(String sum, DataHelper.CardInfo cardInfo) { //верное заполнение полей с возвратом на Dashboard Page
        transfer(sum, cardInfo);
        return new DashboardPage();
    }

    public void errorNotification (String text) {
        errorNotification.shouldHave(Condition.text(text), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }

}
