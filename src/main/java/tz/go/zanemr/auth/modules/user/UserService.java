package tz.go.zanemr.auth.modules.user;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import tz.go.zanemr.auth.core.BaseCrudService;

import java.util.UUID;

public interface UserService extends BaseCrudService<UserDto, Long> {
    void changePassword(UserChangePasswordDto userChangePasswordDto, JwtEncodingContext context);
}
