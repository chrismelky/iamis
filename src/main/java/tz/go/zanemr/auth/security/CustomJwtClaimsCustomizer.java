package tz.go.zanemr.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;
import tz.go.zanemr.auth.modules.user.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().contentEquals("access_token")) {
            Authentication principal = context.getPrincipal();
            log.info(" user authorities : {}", principal.getAuthorities().size());
            Map<String, Object> customClaims = new HashMap<>();
            customClaims.put("authorities",  principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            context.getClaims().claims(claims -> claims.putAll(customClaims));
        }
    }
}
