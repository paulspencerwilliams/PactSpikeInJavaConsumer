package simple.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = "pretty",
        features = "src/test/resources/features",
        glue = {"simple.acceptance.glue"})
public class CucumberTest {
}
