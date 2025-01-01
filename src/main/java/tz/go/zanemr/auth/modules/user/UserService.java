package tz.go.zanemr.auth.modules.user;

import tz.go.zanemr.auth.core.BaseCrudService;

import java.util.UUID;

public interface UserService extends BaseCrudService<UserDto, Long> {
    void changePassword(UserChangePasswordDto userChangePasswordDto);
    void resetPassword(UUID uuid,UserResetPasswordDto userResetPasswordDto);
}
