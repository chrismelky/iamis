package tz.go.zanemr.auth.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class CurrentUserService {

    public CurrentUserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Map<String, Object> details = token.getTokenAttributes();
        CurrentUserDto currentUserDto = new CurrentUserDto();
        currentUserDto.setEmail(Objects.toString(details.get("email"), null));
        currentUserDto.setFirstName(Objects.toString(details.get("firstName")));
        currentUserDto.setLastName(Objects.toString(details.get("lastName")));
        currentUserDto.setMiddleName(Objects.toString(details.get("middleName")));
        String facilityUuid = Objects.toString(details.get("facilityUuid"));
        String userUuid = Objects.toString(details.get("userUuid"));
        currentUserDto.setUuid(userUuid != null ? UUID.fromString(userUuid) : null );
        currentUserDto.setFacilityUuid(details.get("facilityUuid") != null ? UUID.fromString(facilityUuid) : null);
        currentUserDto.setFacilityName(Objects.toString(details.get("facilityName")));
        currentUserDto.setFacilityCode(Objects.toString(details.get("facilityCode")));

        String facilityId = Objects.toString(details.get("facilityId"));
        currentUserDto.setFacilityId(facilityId != null ? Long.parseLong(facilityId) : null);
        String userId = Objects.toString(details.get("userId"));
        currentUserDto.setId(userId != null ? Long.parseLong(userId) : null);

        return currentUserDto;
    }
}
