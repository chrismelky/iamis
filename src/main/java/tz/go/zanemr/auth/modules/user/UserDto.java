package tz.go.zanemr.auth.modules.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tz.go.zanemr.auth.core.BaseDto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String mobileNumber;
    private Boolean isActive;
    private Boolean passwordChanged;
    private List<UUID> roleIds;
    private UUID facilityId;
    private String facilityName;
    private String facilityCode;

}