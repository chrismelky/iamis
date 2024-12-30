package tz.go.zanemr.auth.modules.external_service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tz.go.zanemr.auth.config.OAuthFeignConfig;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;

import java.util.UUID;

@FeignClient(name = "client-registration", url = "http://102.223.7.208:8082", configuration = OAuthFeignConfig.class)
public interface ClientRegistrationFeignClient {

    @GetMapping(AppConstants.API_PREFIX+"/facilities/{uuid}")
    CustomApiResponse findFacilityByUuid(@PathVariable UUID uuid);
}
