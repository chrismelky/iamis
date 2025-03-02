package com.deeptech.iamis.modules.authority;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Authority}
 */
public record AuthorityDto(Long id, UUID uuid, @NotNull String name, String service, String resource, String action,
                           String method, String description) implements Serializable {
}