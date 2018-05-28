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

public class RegistrationTest {
    @Rule
    public PactProviderRuleMk2 mockProvider = new PactProviderRuleMk2("simple-auth-api", "localhost", 8081, this);

    @Pact(consumer = "simple-web")
    public RequestResponsePact createPact(PactDslWithProvider builder) {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "application/json, application/cbor, application/*+json");

        Map<String, String> expectedResponseHeaders = new HashMap<>();
        expectedResponseHeaders.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .given("No users exist")
                    .uponReceiving("valid registration request")
                        .path("/register")
                        .headers(requestHeaders)
                        .body("{\"username\": \"paul\", \"age\": 40, \"password\": \"secret\"}")
                        .method("POST")
                    .willRespondWith()
                        .status(201)
                        .headers(expectedResponseHeaders)
                        .body("{\"id\" : 123, \"username\": \"paul\", \"firstname\": \"Paul\", \"lastname\": \"Williams\"}")
                    .toPact();
    }

    @Test
    @PactVerification()
    public void test() {
        AuthClient sut = new AuthClient();

        RegistrationRequest request = new RegistrationRequest("paul", 40, "secret");
        RegistrationResponse response = sut.register(request);
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatus(), is(RegistrationResponse.Types.SUCCESS));
        assertThat(response.getRegistered().getId(), is(123));
        assertThat(response.getRegistered().getUsername(), is("paul"));
        assertThat(response.getRegistered().getFirstname(), is("Paul"));
        assertThat(response.getRegistered().getLastname(), is("Williams"));
    }
}
