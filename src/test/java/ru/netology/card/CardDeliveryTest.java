package ru.netology.card;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate nextDate = date.plusDays(3);

    @BeforeEach
    void setUp() {
        open("http://0.0.0.0:7777");
    }


    @Test
    void shouldCorrectValues() {

        String dateForm = formatter.format(nextDate);

        $("[data-test-id = 'city'] input").setValue("Воронеж");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(dateForm);
        $("[data-test-id = 'name'] input").setValue("Иванов Петр");
        $("[name = 'phone']").setValue("+79777777777");
        $("[data-test-id = 'agreement']").click();
        $(Selectors.byText("Забронировать")).click();
        $(Selectors.withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id = 'notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно забронирована на " + dateForm));

    }

    @Test
    void IfIncorrectCityValue() {

        $("[data-test-id = 'city'] input").setValue("Voronezh");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(formatter.format(nextDate));
        $("[data-test-id = 'name'] input").setValue("Иванов Петр");
        $("[name = 'phone']").setValue("+79777777777");
        $("[data-test-id = 'agreement']").click();
        $(Selectors.byText("Забронировать")).click();
        $("[data-test-id = 'city'] .input__inner .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void IfIncorrectNameValue() {

        $("[data-test-id = 'city'] input").setValue("Воронеж");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(formatter.format(nextDate));
        $("[data-test-id = 'name'] input").setValue("Ivan.");
        $("[name = 'phone']").setValue("+79777777777");
        $("[data-test-id = 'agreement']").click();
        $(Selectors.byText("Забронировать")).click();
        $("[data-test-id = 'name'] .input__inner .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void IfIncorrectPhoneValue() {

        $("[data-test-id = 'city'] input").setValue("Воронеж");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(formatter.format(nextDate));
        $("[data-test-id = 'name'] input").setValue("Петр Иванович");
        $("[name = 'phone']").setValue("телефон");
        $("[data-test-id = 'agreement']").click();
        $(Selectors.byText("Забронировать")).click();
        $("[data-test-id = 'phone'] .input__inner .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void IfEmptyValueOfName() {

        $("[data-test-id = 'city'] input").setValue("");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(formatter.format(nextDate));
        $("[data-test-id = 'name'] input").setValue("Петр Иванович");
        $("[name = 'phone']").setValue("+79777777777");
        $("[data-test-id = 'agreement']").click();
        $(Selectors.byText("Забронировать")).click();
        $("[data-test-id = 'city'] .input__inner .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void IfNotClickCheckbox() {

        $("[data-test-id = 'city'] input").setValue("Воронеж");
        $("[placeholder = 'Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $("[placeholder = 'Дата встречи']").setValue(formatter.format(nextDate));
        $("[data-test-id = 'name'] input").setValue("Петр Иванович");
        $("[name = 'phone']").setValue("+79777777777");
        $(Selectors.byText("Забронировать")).click();
        $("[data-test-id= 'agreement'] .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
