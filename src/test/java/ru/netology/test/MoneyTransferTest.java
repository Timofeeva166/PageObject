package ru.netology.test;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    public void setup(){
        open("http://localhost:9999");
    }

    @Test
    public void TransferFromFirstToSecond() { //перевод с первой на вторую
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        var sum = "190";

        var transferPage = dashboardPage.chooseCardForTransfer(secondCard);
        dashboardPage = transferPage.ValidTransfer(sum, firstCard);
        dashboardPage.reload();

        assertEquals(firstCardBalance - Integer.parseInt(sum), dashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalance + Integer.parseInt(sum), dashboardPage.getCardBalance(secondCard));
    }

    @Test
    public void TransferFromSecondToFirst() { //перевод с второй на первую
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        var sum = "190";

        var transferPage = dashboardPage.chooseCardForTransfer(firstCard);
        dashboardPage = transferPage.ValidTransfer(sum, secondCard);
        dashboardPage.reload();

        assertEquals(firstCardBalance + Integer.parseInt(sum), dashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalance - Integer.parseInt(sum), dashboardPage.getCardBalance(secondCard));
    }

    @Test
    public void transferFromCardWithInvalidNumber() { //проверяет, может ли быть переведена сумма с карты с невалидным номером
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var incorrectCard = DataHelper.getIncorrectCardInfo();
        var sum = "190";

        var transferPage = dashboardPage.chooseCardForTransfer(firstCard);
        transferPage.Transfer(sum, incorrectCard);

        $("[data-test-id = error-notification]").shouldBe(Condition.visible);
    }

    @Test
    public void transferWithKopecks() { //перевод суммы с дробной частью (копейками)
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        var sum = "55.77";

        var transferPage = dashboardPage.chooseCardForTransfer(firstCard);
        dashboardPage = transferPage.ValidTransfer(sum, secondCard);
        dashboardPage.reload();

        assertEquals(firstCardBalance + Double.parseDouble(sum), dashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalance - Double.parseDouble(sum), dashboardPage.getCardBalance(secondCard));
    }

    @Test
    public void transferSumMoreThatIsInTheCard() { //перевод большей суммы денег, чем есть на карте
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        var dashboardPage = verificationPage.validVerify(verificationCode);

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);

        var sum = String.valueOf(firstCardBalance + 100);

        var transferPage = dashboardPage.chooseCardForTransfer(secondCard);
        transferPage.Transfer(sum, firstCard);

        $("[data-test-id = error-notification]").shouldHave(Condition.exactText("Недостаточно " +
                "средств на счёте. Измените сумму или пополните счет.")).shouldBe(Condition.visible);
    }
}
