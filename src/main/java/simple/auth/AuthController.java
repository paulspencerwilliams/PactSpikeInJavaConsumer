package simple.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthClient authClient;

    @Autowired
    AuthController(AuthClient authClient) {

        this.authClient = authClient;
    }

    @GetMapping("/loginForm")
    public String index(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "loginForm";
    }

    @PostMapping("/login")
    public String submitLogin(@ModelAttribute Credentials credentials, Model model) {
        User user = authClient.login(credentials.getUsername(), credentials.getPassword());
        model.addAttribute("user", user);
        return "homepage";
    }

}