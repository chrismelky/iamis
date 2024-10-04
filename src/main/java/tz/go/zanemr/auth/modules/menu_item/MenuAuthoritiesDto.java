package tz.go.zanemr.auth.modules.menu_item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MenuAuthoritiesDto {

    private UUID menuItemUuid;
    private Set<UUID> authorityIds = new HashSet<>();
}
