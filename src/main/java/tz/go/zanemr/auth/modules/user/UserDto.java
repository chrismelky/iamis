package tz.go.zanemr.auth.modules.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tz.go.zanemr.auth.core.BaseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    @NotNull
    @Email
    private String email;
    @NotNull
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String mobileNumber;
    private Boolean isActive = Boolean.TRUE;
    private Boolean passwordChanged = Boolean.FALSE;
    private List<UUID> roleIds = new ArrayList<>();
    private UUID facilityId;
    private String facilityName;
    private String facilityCode;

}