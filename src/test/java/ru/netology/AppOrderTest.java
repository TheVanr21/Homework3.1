package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AppOrderTest {

    WebDriver driver;
    String expectedInputError = "input_invalid";

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
//        driver = new ChromeDriver(options);
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSend() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Рохлин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79815553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldSendWithFIOWithDash() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Рохлин-Первый Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79815553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotSendWithoutFIO() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Поле обязательно для заполнения";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=name]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithoutPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Рохлин Василий");
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Поле обязательно для заполнения";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=phone]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithoutAgreement() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Рохлин Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79815553535");
        driver.findElement(By.cssSelector("button")).click();

        Boolean actual = driver.findElement(By.cssSelector("[data-test-id=agreement]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(true, actual);
    }

    @Test
    public void shouldNotSendWithNotRussianFIO() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Vasily Rokhlin");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79815553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=name]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithFIOWithSymbols() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Рохлин6");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79815553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=name]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithLongerPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Рохлин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+798155535353");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=phone]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithShorterPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Рохлин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7981555353");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=phone]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithoutPlusInPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Рохлин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79815553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=phone]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }

    @Test
    public void shouldNotSendWithWrongPlusInPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Рохлин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79815+553535");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();

        String expectedString = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualString = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub")).getText().trim();

        Boolean expectedErrorContent = true;
        Boolean actualErrorContent = driver.findElement(By.cssSelector("[data-test-id=phone]")).getAttribute("class").contains(expectedInputError);

        Assertions.assertEquals(expectedString, actualString);
        Assertions.assertEquals(expectedErrorContent, actualErrorContent);
    }
}
