package tz.go.zanemr.auth.modules.menu_group;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tz.go.zanemr.auth.modules.menu_item.MenuItemDto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link MenuGroup}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MenuGroupDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private  Long id;
    private  UUID uuid;
    private  @NotNull String name;
    private  @NotNull String module;
    private  String state;
    private  String icon;
    private  @NotNull Integer sortOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuItemDto> children;


    public MenuGroupDto(Long id, UUID uuid, @NotNull String name, @NotNull String name1, @NotNull String state, String icon, @NotNull Integer sortOrder) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.state = state;
        this.icon = icon;
        this.sortOrder = sortOrder;

    }
}