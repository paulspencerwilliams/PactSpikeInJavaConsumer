package simple.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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
        LoginResponse response = authClient.login(credentials.getUsername(), credentials.getPassword());
        switch (response.getStatus()) {
            case SUCCESS:
                model.addAttribute("user", response.getLoggedInUser());
                return "homepage";
            case BAD_CREDENTIALS:
                model.addAttribute("message", "Bad credentials, try again");
                return "loginForm";
            default:
                throw new RuntimeException("Unhandled scenario");
        }
    }

    @GetMapping("/registrationForm")
    public String index(RegistrationForm registrationForm) {
        return "registrationForm";
    }

    @PostMapping("/register")
    public String submitRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registrationForm";
        }

        RegistrationResponse response = authClient.register(registrationForm.toRequest());
        model.addAttribute("user", response.getRegistered());
        model.addAttribute("emailUnverified", true);
        return "homepage";
    }

}