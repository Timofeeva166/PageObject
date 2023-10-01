package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement page = $("[data-test-id = dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() { //проверка на то, что страница открылась
        page.shouldBe(Condition.visible);
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardIndex())).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage chooseCardForTransfer(DataHelper.CardInfo cardInfo) {
        cards.findBy(Condition.attribute("data-test-id", cardInfo.getCardIndex())).$("button").click();;
        return new TransferPage();
    }

    public void reload() {
        $("[data-test-id = action-reload]").click();
    }
}
