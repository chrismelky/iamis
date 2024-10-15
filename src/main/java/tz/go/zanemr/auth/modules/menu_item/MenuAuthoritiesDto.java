package tz.go.zanemr.auth.modules.menu_item;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MenuAuthoritiesDto {

    @NotNull
    private UUID menuItemId;
    private Set<UUID> authorityIds = new HashSet<>();
}
