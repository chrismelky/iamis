package tz.go.zanemr.auth.modules.external_service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.zanemr.auth.core.CustomApiResponse;
import tz.go.zanemr.auth.core.NoAuthorization;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign")
public class FeignTestController {

    private final ClientRegistrationFeignClient client;

    @GetMapping("/facility/{uuid}")
    @NoAuthorization
    public CustomApiResponse facility(@PathVariable UUID uuid) {
        return CustomApiResponse.ok(client.findFacilityByUuid(uuid).getData());
    }
}
