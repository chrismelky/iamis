package tz.go.zanemr.auth.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class OAuthFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String token = (String) authentication.getCredentials();
            return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + token);
        }
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer ");
        };
    }
}
