package tz.go.zanemr.auth.modules.user;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX+"/users")
public class UserResource {

   private final UserService userService;

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

    @PostMapping("/change-password")
    public CustomApiResponse changePassword(
            @Valid @RequestBody UserChangePasswordDto userChangePasswordDto) {

        userService.changePassword(userChangePasswordDto);

        return CustomApiResponse.ok("Password updated successfully");
    }

    @PutMapping("/{uuid}/reset-password")
    public CustomApiResponse resetPassword(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody UserResetPasswordDto userResetPasswordDto
    ) {

        userService.resetPassword(uuid,userResetPasswordDto);

        return CustomApiResponse.ok("Password updated successfully");
    }
}
