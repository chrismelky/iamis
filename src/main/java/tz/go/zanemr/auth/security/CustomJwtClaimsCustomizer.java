package tz.go.zanemr.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;
import tz.go.zanemr.auth.core.BaseModel;
import tz.go.zanemr.auth.modules.user.User;
import tz.go.zanemr.auth.modules.user.UserDto;
import tz.go.zanemr.auth.modules.user.UserMapper;
import tz.go.zanemr.auth.modules.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().contentEquals("access_token")) {

            Authentication principal = context.getPrincipal();
            User user = userRepository.findUserByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

            log.info(" user authorities : {}", principal.getAuthorities().size());
            Map<String, Object> customClaims = new HashMap<>();
            customClaims.put("facilityId", user.getFacilityId() != null ? user.getFacilityId().toString() : null);
            customClaims.put("facilityName", user.getFacilityName());
            customClaims.put("userId", user.getUuid().toString());
            customClaims.put("firstName", user.getFirstName());
            customClaims.put("lastName", user.getLastName());
            customClaims.put("middleName", user.getMiddleName());
            customClaims.put("facilityCode", user.getFacilityCode());
            customClaims.put("roleIds", user.getRoles().stream().map(r->r.getId().toString()).toList());
//            customClaims.put("authorities", new ArrayList<>());
            customClaims.put("authorities", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            context.getClaims().claims(claims -> claims.putAll(customClaims));
        }
    }
}
