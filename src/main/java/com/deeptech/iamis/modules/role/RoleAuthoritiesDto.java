package com.deeptech.iamis.modules.role;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthoritiesDto {

    private UUID roleId;
    private Set<UUID> authorityIds = new HashSet<>();
}
