package tz.go.zanemr.auth.modules.role;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleAuthorities {

    private UUID roleId;
    private Set<Long> authorityIds = new HashSet<>();
}
