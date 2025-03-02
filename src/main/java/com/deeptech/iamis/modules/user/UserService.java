package com.deeptech.iamis.modules.user;

import com.deeptech.iamis.core.BaseCrudService;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseCrudService<UserDto, Long> {
    void changePassword(UserChangePasswordDto userChangePasswordDto);
    void resetPassword(UUID uuid,UserResetPasswordDto userResetPasswordDto);
    List<UserDto> searchByFullName(String fullname);
}
