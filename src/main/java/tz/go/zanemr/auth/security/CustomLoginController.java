package tz.go.zanemr.auth.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomLoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
