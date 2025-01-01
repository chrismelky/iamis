package tz.go.zanemr.auth.modules.user;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;
import tz.go.zanemr.auth.modules.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX+"/users")
public class UserResource {

   private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping()
    public CustomApiResponse create(@Valid @RequestBody UserDto userDto) {
        if(userDto.getId() != null || userDto.getUuid() != null) {
            throw new ValidationException("New user should not have an id or uuid value");
        }

        return CustomApiResponse.ok("User created", userService.save(userDto));
    }

    @PutMapping("/{uuid}")
    public CustomApiResponse update(@PathVariable("uuid") UUID uuid,
                                    @Valid @RequestBody UserDto userDto) {
        if(userDto.getUuid() == null) {
            throw new ValidationException("Update user should have an uuid");
        }
        if(!uuid.equals(userDto.getUuid())) {
            throw new ValidationException("Path variable uuid should be equal to user uuid");
        }
        return CustomApiResponse.ok("User updated", userService.save(userDto));
    }

    @GetMapping
    public CustomApiResponse get(Pageable pageable,@RequestParam Map<String, Object> searchParams) {
        return CustomApiResponse.ok( userService.findAll(pageable, searchParams));
    }

    @GetMapping("/{uuid}")
    public CustomApiResponse get(@PathVariable("uuid") UUID uuid) {
        return CustomApiResponse.ok(userService.findById(uuid));
    }

    @DeleteMapping("/{uuid}")
    public CustomApiResponse delete(@PathVariable("uuid") UUID uuid) {
        userService.delete(uuid);
        return CustomApiResponse.ok("User deleted successfully");
    }

    @PutMapping("/change-password")
    public CustomApiResponse changePassword(
            @Valid @RequestBody UserChangePasswordDto userChangePasswordDto,JwtEncodingContext context) {

        Authentication principal = context.getPrincipal();
        User user = userRepository.findUserByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        if (userChangePasswordDto.getCurrentPassword() == null || userChangePasswordDto.getNewPassword() == null || userChangePasswordDto.getConfirmPassword() == null) {
            throw new ValidationException("Current password, new password, and confirm password are required.");
        }

        if (!userChangePasswordDto.getNewPassword().equals(userChangePasswordDto.getConfirmPassword())) {
            throw new ValidationException("New password and confirm password do not match.");
        }

        userService.changePassword(userChangePasswordDto,context);

        return CustomApiResponse.ok("Password updated successfully");
    }
}
