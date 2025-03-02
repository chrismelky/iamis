package com.deeptech.iamis.modules.role;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.deeptech.iamis.core.SearchService;
import com.deeptech.iamis.core.Utils;
import com.deeptech.iamis.modules.authority.AuthorityRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Service implementation for managing roles. Provides CRUD operations for roles
 * and the functionality to assign authorities to roles.
 * This service uses {@link RoleRepository} for database operations and {@link RoleMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for roles.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl extends SearchService<Role> implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final AuthorityRepository authorityRepository;

    /**
     * Creates or updates a role based on the presence of a UUID in the DTO.
     *
     * @param roleDto the {@link RoleDto} containing role data.
     * @return the saved {@link RoleDto}.
     * @throws ValidationException if the role is not found for update or there is validation failure.
     */
    @Override
    public RoleDto save(RoleDto roleDto) {
        // Convert the DTO to an entity object
        Role role = roleMapper.toEntity(roleDto);

        // If the role has a UUID, fetch the existing role and perform an update
        if (roleDto.getUuid() != null) {
            role = roleRepository.findByUuid(roleDto.getUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));

            // Partially update the existing role with new data from the DTO
            role = roleMapper.partialUpdate(roleDto, role);
        } else {
            // Set a new UUID for a new role
            role.setUuid(Utils.generateUuid());
        }
        // Save the role and return the DTO
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    /**
     * Finds all roles based on pagination and search criteria.
     *
     * @param pageable the pagination information.
     * @param search   a map of search criteria.
     * @return a paginated list of {@link RoleDto}.
     */
    @Override
    public Page<RoleDto> findAll(Pageable pageable,
                                 Map<String, Object> search) {
        return roleRepository.findAll(createSpecification(Role.class, search), pageable)
                .map(roleMapper::toDto);
    }

    /**
     * Finds a role by its UUID.
     *
     * @param uuid the UUID of the role to find.
     * @return the {@link RoleDto} if found.
     * @throws ValidationException if the role is not found.
     */
    @Override
    public RoleDto findById(UUID uuid) {
        return roleRepository.findByUuid(uuid)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    /**
     * Deletes a role by its UUID.
     *
     * @param uuid the UUID of the role to delete.
     * @throws ValidationException if the role cannot be deleted.
     */
    @Override
    public void delete(UUID uuid) {
        try {
            roleRepository.deleteByUuid(uuid);
        } catch (Exception e) {
            throw new ValidationException("Can not delete role");
        }
    }

    /**
     * Assigns a set of authorities to a role.
     *
     * @param dto the {@link RoleAuthoritiesDto} containing the role ID and authority IDs.
     * @return the updated {@link RoleDto} after authorities have been assigned.
     * @throws ValidationException if the role is not found or validation fails.
     */
    @Override
    public RoleDto assignAuthorities(RoleAuthoritiesDto dto) {
        Role role = roleRepository.findByUuid(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role with id "+dto.getRoleId()+" not found"));
        role.setAuthorities(new HashSet<>());
        for (UUID authId : dto.getAuthorityIds()) {
            role.addAuthority(authorityRepository.getReferenceByUuid(authId));
        }
        roleRepository.save(role);
        return roleMapper.toDto(role);
    }
}
