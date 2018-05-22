package simple.acceptance.glue;

import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import simple.Application;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepDefs {
    @Value("${app.baseurl}")
    private String baseurl;

    @When("^I authenticate$")
    public void iAuthenticate() throws Throwable {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        driver.navigate().to(baseurl + "/loginForm");

        driver.findElement(By.id("txtUsername")).sendKeys("Paul");
        driver.findElement(By.id("txtPassword")).sendKeys("Secret");
        driver.findElement(By.id("btnLogin")).click();
    }

    @Then("^I will be welcomed personally$")
    public void iWillBeWelcomedPersonally() throws Throwable {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        driver.navigate().to(baseurl);
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText, is("Greetings..."));
    }

    @After
    public void closeBrowser() {
        DriverFactory.getInstance().getDriver().quit();
    }
}
