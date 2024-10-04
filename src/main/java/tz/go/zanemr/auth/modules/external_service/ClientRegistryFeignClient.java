package tz.go.zanemr.auth.modules.external_service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tz.go.zanemr.auth.core.CustomApiResponse;

import java.util.UUID;

@FeignClient(name = "client-registry")
public interface ClientRegistryFeignClient {

    @GetMapping("/facilities/{uuid}")
    CustomApiResponse findByUuid(UUID uuid);
}
