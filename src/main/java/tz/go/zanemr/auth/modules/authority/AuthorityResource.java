package tz.go.zanemr.auth.modules.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;
import tz.go.zanemr.auth.core.NoAuthorization;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX+"/authorities")
public class AuthorityResource {

    private final AuthorityService authorityService;

    @GetMapping
    @NoAuthorization
    public CustomApiResponse getAuthorities() {
        return CustomApiResponse.ok(authorityService.findAll());
    }
}
