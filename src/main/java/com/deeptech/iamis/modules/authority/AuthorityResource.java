package com.deeptech.iamis.modules.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deeptech.iamis.core.AppConstants;
import com.deeptech.iamis.core.CustomApiResponse;
import com.deeptech.iamis.core.NoAuthorization;

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
