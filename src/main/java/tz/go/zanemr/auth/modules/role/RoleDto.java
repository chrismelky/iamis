package tz.go.zanemr.auth.modules.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tz.go.zanemr.auth.modules.core.BaseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Role}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDto extends BaseDto {
    @NotNull
    @NotEmpty
    private String name;
    private String code;
    private List<UUID> authorityIds = new ArrayList<>();
}