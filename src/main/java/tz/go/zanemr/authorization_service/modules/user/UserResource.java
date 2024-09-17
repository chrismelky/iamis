package tz.go.zanemr.authorization_service.modules.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.zanemr.authorization_service.modules.core.AppConstants;

@RestController
@RequestMapping(AppConstants.API_PREFIX+"/users")
public class UserResource {


    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
