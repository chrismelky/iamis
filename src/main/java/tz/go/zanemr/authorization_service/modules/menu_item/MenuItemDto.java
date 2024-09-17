package tz.go.zanemr.authorization_service.modules.menu_item;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link MenuItem}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto implements Serializable {

    private Long id;
    private UUID uuid;
    @NotNull
    private String name;
    private String icon;
    @NotNull
    private String state;
    @NotNull
    private Integer sortOrder;
    private String translationLabel;

    @NotNull
    private Long menuGroupId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> authorityIds;
}