package tz.go.zanemr.auth.modules.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tz.go.zanemr.auth.core.BaseDto;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class UserChangePasswordDto extends BaseDto{
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
