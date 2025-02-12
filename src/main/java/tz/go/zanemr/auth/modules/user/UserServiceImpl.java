package tz.go.zanemr.auth.modules.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.core.*;
import tz.go.zanemr.auth.modules.external_service.ClientRegistrationFeignClient;
import tz.go.zanemr.auth.modules.role.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends SearchService<User> implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final CurrentUserService currentUserService;

    @Value("${zanemr.default-password:password}")
    private String defaultPassword;

    @Override
    public UserDto save(UserDto dto) {
        User user = userMapper.toEntity(dto);
        if (dto.getUuid() != null) {
            user = userRepository.findByUuid(dto.getUuid())
                    .orElseThrow(
                            () -> new EntityNotFoundException("User with id " + dto.getUuid() + " not found"));
            user = userMapper.partialUpdate(dto, user);
        } else {
            user.setUuid(Utils.generateUuid());
            user.setPassword(passwordEncoder.encode(defaultPassword));
        }
        if(dto.getRoleIds() != null) {
            user.setRoles(new HashSet<>());
            for (UUID roleId : dto.getRoleIds()) {
                user.addRole(roleRepository.getReferenceByUuid(roleId));
            }
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

//    /**
//     * Get facility details form client registration service
//     *
//     * @param user entity to update facility detail
//     * @param facilityId facility UUID
//     * @return @User
//     */
//    private User setUserFacility(User user, UUID facilityId) {
//        if (facilityId != null) {
//            try {
//                CustomApiResponse response = crFeignClient.findFacilityByUuid(facilityId);
//                Map<String, Object> facility = (Map<String, Object>) response.getData();
//                user.setFacilityCode((String) facility.get("code"));
//                user.setFacilityName((String) facility.get("name"));
//
//            } catch (FeignException.NotFound e) {
//                throw new ValidationException("Facility with uuid " + facilityId + " not found");
//            }
//        }
//        return user;
//    }

    @Override
    public Page<UserDto> findAll(Pageable pageable, Map<String, Object> searchParams) {
        return userRepository.findAll(createSpecification(User.class, searchParams), pageable)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto findById(UUID uuid) {
        return userRepository.findByUuid(uuid).map(userMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("User with id " + uuid + " not found")
        );
    }

    @Override
    public void delete(UUID uuid) {
        try {
            userRepository.deleteByUuid(uuid);
        } catch (Exception e) {
            throw new ValidationException("Cannot delete user with id " + uuid);
        }
    }

    @Override
    public void changePassword(UserChangePasswordDto userChangePasswordDto) {

//        CurrentUserDto userDto = currentUserService.getCurrentUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findUserByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " +authentication.getName() + " not found"));

        if (!passwordEncoder.matches(userChangePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new ValidationException("Current password is incorrect");
        }

        if (!userChangePasswordDto.getNewPassword().equals(userChangePasswordDto.getConfirmPassword())) {
            throw new ValidationException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        user.setPasswordChanged(true);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(UUID uuid,UserResetPasswordDto userResetPasswordDto){
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new EntityNotFoundException("User with UUID " + uuid + " not found")
        );
        user.setPassword(passwordEncoder.encode(userResetPasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserDto> searchByFullName(String fullname) {

        List<User> users = userRepository.searchByFullName(fullname);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countFacilityUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " +authentication.getName() + " not found"));

        if (user.getFacilityId() == null) {
            return userRepository.count();
        } else {

            long facilityId = user.getFacilityId();
            return userRepository.countFacilityUser(facilityId);
        }
    }


}
