package tz.go.zanemr.authorization_service.modules.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id,
                      UUID uuid,
                      String email,
                      String firstName,
                      String middleName,
                      String lastName,
                      String sex,
                      String mobileNumber,
                      Boolean isActive,
                      Boolean passwordChanged,
                      List<UUID> roleIds) implements Serializable {
}