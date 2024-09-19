package tz.go.zanemr.auth.modules.role;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.modules.authority.AuthorityRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final AuthorityRepository authorityRepository;

    @Override
    public RoleDto save(RoleDto roleDto) {
        Role role;
        if (roleDto.getUuid() != null) {
            role = roleRepository.findByUuid(roleDto.getUuid())
                    .orElseThrow(() -> new ValidationException("Role not found"));
        } else {
            role= new Role();
        }
        role = roleMapper.partialUpdate(roleDto, role);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable, Map<String, Object> search) {
        return roleRepository.findAll(pageable).map(roleMapper::toDto);
    }

    @Override
    public RoleDto findById(UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public RoleDto assignAuthorities(RoleAuthorities roleAuthorities) {
        Role role = roleRepository.findByUuid(roleAuthorities.getRoleId())
                .orElseThrow(() -> new ValidationException("Role not found"));
        role.setAuthorities(new HashSet<>());
        for (Long authId : roleAuthorities.getAuthorityIds()) {
            role.addAuthority(authorityRepository.getReferenceById(authId));
        }
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }
}
