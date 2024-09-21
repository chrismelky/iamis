package tz.go.zanemr.auth.modules.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.core.SearchService;
import tz.go.zanemr.auth.core.Utils;
import tz.go.zanemr.auth.modules.role.RoleRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends SearchService<User> implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @Value("${zanemr.default-password:password}")
    private String defaultPassword;

    @Override
    public UserDto save(UserDto dto) {
        User user = userMapper.toEntity(dto);
        if(dto.getUuid() != null) {
            user = userRepository.findByUuid(dto.getUuid())
                    .orElseThrow(
                            ()-> new EntityNotFoundException("User with id " + dto.getUuid() + " not found"));
            user = userMapper.partialUpdate(dto, user);
            user.setRoles(new HashSet<>());
        } else {
            user.setUuid(Utils.generateUuid());
            user.setPassword(passwordEncoder.encode(defaultPassword));
        }
        for (UUID roleId : dto.getRoleIds()) {
            user.addRole(roleRepository.getReferenceByUuid(roleId));
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable, Map<String, Object> searchParams) {
        return userRepository.findAll(createSpecification(User.class, searchParams),pageable)
                .map(userMapper::toDto);
    }

    @Override
    public UserDto findById(UUID uuid) {
        return userRepository.findByUuid(uuid).map(userMapper::toDto).orElseThrow(
                ()-> new EntityNotFoundException("User with id " + uuid + " not found")
        );
    }

    @Override
    public void delete(UUID uuid) {
        try {
            userRepository.deleteByUuid(uuid);
        }catch (Exception e) {
            throw new ValidationException("Cannot delete user with id " + uuid);
        }
    }
}
