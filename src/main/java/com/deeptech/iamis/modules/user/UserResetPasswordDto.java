package com.deeptech.iamis.modules.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.deeptech.iamis.core.BaseDto;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserResetPasswordDto extends BaseDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String newPassword;

}
