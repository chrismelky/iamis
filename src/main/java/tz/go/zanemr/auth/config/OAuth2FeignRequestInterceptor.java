package tz.go.zanemr.auth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("Feign: Get user Authentication: {}", authentication);

        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            log.info("Request with token: {}", token);
            requestTemplate.header("Authorization", "Bearer " + token);
        }

    }
}
