package com.deeptech.iamis.modules.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.deeptech.iamis.core.BaseDto;

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
    private List<UUID> roleIds;


    public UserDto(
            Long id,
            UUID uuid,
            String firstName,
            String middleName,
            String lastName) {

        this.setId(id);
        this.setUuid(uuid);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

}