package tz.go.zanemr.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tz.go.zanemr.auth.core.NoAuthorization;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class CustomAuthorizationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!handler.getClass().getSimpleName().equals("ResourceHttpRequestHandler")
                && request.getRequestURI().contains("api/")) {
            log.info("******Intercepting****");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Set<String> userAuthorities = new HashSet<>();
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                userAuthorities.addAll(((Jwt) authentication.getPrincipal()).getClaim("authorities"));
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoAuthorization noAuthorization = handlerMethod.getMethodAnnotation(NoAuthorization.class);

            if (noAuthorization == null) {
                String resourceName = handlerMethod.getBeanType().getSimpleName().replace("Resource", "");
                String actionName = handlerMethod.getMethod().getName();
                String authName = resourceName.toUpperCase().concat("_").concat(actionName.toUpperCase());

                log.info("Finding Authority");
                if (userAuthorities.contains(authName)) {
                    return true;
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response
                            .getWriter()
                            .write(
                                    "{\"message\":\"Your Are Not Authorized to Access {"
                                            + actionName
                                            + "} on {"
                                            + resourceName
                                            + "}\", \"errors\":[\"Your Are Not Authorized to Access {"
                                            + actionName
                                            + "} on {"
                                            + resourceName
                                            + "}\"]}");
                    response.flushBuffer();
                    return false;
                }

            } else {
                return true;
            }
        }

        return true;
    }
}
