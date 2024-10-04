package tz.go.zanemr.auth.modules.role;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleAuthoritiesDto {

    private UUID roleId;
    private Set<UUID> authorityIds = new HashSet<>();
}
