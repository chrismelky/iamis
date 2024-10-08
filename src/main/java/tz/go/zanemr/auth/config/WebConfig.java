package tz.go.zanemr.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tz.go.zanemr.auth.security.CustomAuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomAuthorizationInterceptor());
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
