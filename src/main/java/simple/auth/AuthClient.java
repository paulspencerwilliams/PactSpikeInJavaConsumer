package simple.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class AuthClient {
    private static final Logger log = LoggerFactory.getLogger(AuthClient.class);

    public User login(String username, String password) {

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8081/login")
                .queryParam("username", username)
                .queryParam("password", password)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, User.class);
    }
}
