package tz.go.zanemr.authorization_service.modules.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tz.go.zanemr.authorization_service.modules.role.Role;
import tz.go.zanemr.authorization_service.modules.role.RoleRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Value("${zanemr.default-password:password}")
    private String defaultPassword;

    @Override
    public UserDto save(UserDto dto) {
        User user;
        if(dto.uuid() != null) {
            user = userRepository.findById(dto.id())
                    .orElseThrow(
                            ()-> new ValidationException("User with id " + dto.uuid() + " not found"));
            user = userMapper.partialUpdate(dto, user);
            user.setRoles(new HashSet<>());
        } else {
            user = userMapper.toEntity(dto);
            user.setPassword(passwordEncoder.encode(defaultPassword));
        }
        for (UUID roleId : dto.roleIds()) {
            user.addRole(roleRepository.getReferenceByUuid(roleId));
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable, Map<String, Object> search) {
        return null;
    }

    @Override
    public UserDto findById(UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
}
