package simple.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthClient {
    private static final Logger log = LoggerFactory.getLogger(AuthClient.class);

    public User login(String username, String password) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8081/login");

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(builder.build().toUri(), User.class);
    }
}
