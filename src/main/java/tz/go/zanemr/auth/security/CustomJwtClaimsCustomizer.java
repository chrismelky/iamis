package tz.go.zanemr.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;
import tz.go.zanemr.auth.modules.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomJwtClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserRepository userRepository;

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().contentEquals("id_token")) {
            Authentication principal = context.getPrincipal();
            Map<String, Object> customClaims = new HashMap<>();
            customClaims.put("facilityId", "facility_id");
            customClaims.put("authorities", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority));
            context.getClaims().claims(claims -> claims.putAll(customClaims));
        }
    }
}
