package simple.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static simple.auth.LoginResponse.Types.BAD_CREDENTIALS;

@Service
public class AuthClient {
    private static final Logger log = LoggerFactory.getLogger(AuthClient.class);

    public LoginResponse login(String username, String password) {

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8081/login")
                .queryParam("username", username)
                .queryParam("password", password)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        User loggedInUser;
        try {
            loggedInUser = restTemplate.getForObject(uri, User.class);
            return new LoginResponse(loggedInUser);
        }
        catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new LoginResponse(BAD_CREDENTIALS);
            }
            throw new RuntimeException("Unexpected response");
        }
    }
}
