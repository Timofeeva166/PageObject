package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private SelenideElement code = $("[data-test-id = code] input");
    private SelenideElement verifyButton = $("[data-test-id = action-verify]");

    public VerificationPage() { //проверка на то, что страница верификации открылась
        code.shouldBe(Condition.visible);
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        code.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }
}
