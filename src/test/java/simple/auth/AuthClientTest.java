package simple.auth;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AuthClientTest {

    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("simple-auth-api", "localhost", 8081, this);

    @Pact(consumer = "simple-web")
    public RequestResponsePact createPact(PactDslWithProvider builder) {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "application/json, application/cbor, application/*+json");

        Map<String, String> expectedResponseHeaders = new HashMap<>();
        expectedResponseHeaders.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .given("Paul exists with password Secret")
                    .uponReceiving("valid authentication details")
                    .path("/login")
                    .query("username=Paul&password=Secret")
                    .headers(requestHeaders)
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(expectedResponseHeaders)
                    .body("{\"id\":123,\"username\":\"paul\",\"firstname\":\"Paul\",\"lastname\":\"Williams\"}")
                .toPact();
    }

    @Test
    @PactVerification()
    public void successfulLogin() {
        User login = new AuthClient().login("Paul", "Secret");
        assertThat(login, is(notNullValue()));
        assertThat(login.getId(), is(123));
        assertThat(login.getUsername(), is("paul"));
        assertThat(login.getFirstname(), is("Paul"));
        assertThat(login.getLastname(), is("Williams"));
    }
}
