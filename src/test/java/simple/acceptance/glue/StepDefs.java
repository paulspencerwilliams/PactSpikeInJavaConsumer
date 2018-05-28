package simple.acceptance.glue;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import simple.Application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepDefs  {
    private WireMockServer wireMockServer;
    private WebDriver driver;

    @Value("${app.baseurl}")
    private String baseurl;

    @When("^I authenticate$")
    public void iAuthenticate() throws Throwable {
        wireMockServer
                .stubFor(get(urlPathMatching("/login*"))
                        .withQueryParam("username",  equalTo("Paul"))
                        .withQueryParam("password", equalTo("Secret"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"id\":123,\"username\":\"paul\",\"firstname\":\"Paul\",\"lastname\":\"Williams\"}")));

        driver.navigate().to(baseurl + "/loginForm");

        driver.findElement(By.id("txtUsername")).sendKeys("Paul");
        driver.findElement(By.id("txtPassword")).sendKeys("Secret");
        driver.findElement(By.id("btnLogin")).click();
    }

    @Then("^I will be welcomed personally$")
    public void iWillBeWelcomedPersonally() throws Throwable {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyText, is("Hi, Paul Williams"));
    }

    @When("^I attempt to log in with the wrong password$")
    public void iAttemptToLogInWithTheWrongPassword() throws Throwable {
        wireMockServer
                .stubFor(get(urlPathMatching("/login*"))
                        .withQueryParam("username",  equalTo("Paul"))
                        .withQueryParam("password", equalTo("Unconcealed"))
                        .willReturn(aResponse()
                                .withStatus(401)));

        driver.navigate().to(baseurl + "/loginForm");

        driver.findElement(By.id("txtUsername")).sendKeys("Paul");
        driver.findElement(By.id("txtPassword")).sendKeys("Unconcealed");
        driver.findElement(By.id("btnLogin")).click();
    }

    @Then("^I will be prompted to try again suggesting why$")
    public void iWillBePromptedToTryAgainSuggestingWhy() throws Throwable {
        assertThat(driver.findElement(By.id("errorMessage")).getText(), is("Bad credentials, try again"));
    }

    @Before
    public void before() {
        wireMockServer = new WireMockServer(options().port(8081));
        wireMockServer.start();
        driver = DriverFactory.getInstance().getDriver();

    }

    @After
    public void after() {
        wireMockServer.stop();
        DriverFactory.getInstance().removeDriver();
    }
}
