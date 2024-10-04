package tz.go.zanemr.auth.modules.role;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller that provides endpoints for managing roles within the system.
 * This class handles CRUD operations for roles and provides an API to assign authorities to roles.
 * <p>
 * Endpoints:
 * - POST /api/roles: Create a new role
 * - PUT /api/roles/{uuid}: Update an existing role
 * - GET /api/roles: Fetch all roles with optional search and pagination
 * - DELETE /api/roles/{uuid}: Delete a role by its UUID
 * - POST /api/roles/assign-authorities: Assign authorities to a role
 * <p>
 * Uses {@link RoleService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/roles")
public class RoleResource {

    private final RoleService roleService;

    /**
     * Creates a new role in the system.
     *
     * @param dto the {@link RoleDto} object representing the role data.
     * @return {@link CustomApiResponse} containing the created role.
     * @throws ValidationException if the provided role DTO contains an id or uuid (new roles should not have an id or uuid).
     */
    @PostMapping
    public CustomApiResponse create(@Valid @RequestBody RoleDto dto) {
        if (dto.getId() != null || dto.getUuid() != null) {
            throw new ValidationException("New role should not have an id or uuid");
        }
        return CustomApiResponse.ok(roleService.save(dto));
    }


    /**
     * Updates an existing role.
     *
     * @param dto  the {@link RoleDto} object containing updated role data.
     * @param uuid the UUID of the role to update.
     * @return {@link CustomApiResponse} containing the updated role.
     * @throws ValidationException if the provided role DTO does not have an uuid or if the UUIDs do not match.
     */
    @PutMapping("/{uuid}")
    public CustomApiResponse update(@Valid @RequestBody RoleDto dto,
                                    @PathVariable UUID uuid) {
        if (dto.getUuid() == null) {
            throw new ValidationException("Update role should have an uuid value");
        }
        if (!uuid.equals(dto.getUuid())) {
            throw new ValidationException("uuid should be equal to role uuid");
        }
        return CustomApiResponse.ok(roleService.save(dto));
    }

    /**
     * Retrieves a list of roles with pagination and optional search parameters.
     *
     * @param pageable     the {@link Pageable} object to control pagination.
     * @param searchParams a map of search parameters (fields names) for filtering roles.
     * @return {@link CustomApiResponse} containing a list of roles and pagination information.
     */
    @GetMapping
    public CustomApiResponse get(Pageable pageable, @RequestParam Map<String, Object> searchParams) {
        return CustomApiResponse.ok(roleService.findAll(pageable, searchParams));
    }

    /**
     * Retrieves a role by its UUID.
     *
     * @param uuid the UUID of the role to retrieve.
     * @return {@link CustomApiResponse} containing the role data.
     * @throws ValidationException if no role with the given UUID is found.
     */
    @GetMapping("/{uuid}")
    public CustomApiResponse findById(@PathVariable UUID uuid) {
        return CustomApiResponse.ok(roleService.findById(uuid));
    }

    /**
     * Deletes a role by its UUID.
     *
     * @param uuid the UUID of the role to delete.
     * @return {@link CustomApiResponse} confirming the successful deletion.
     */
    @DeleteMapping("/{uuid}")
    public CustomApiResponse delete(@PathVariable UUID uuid) {
        roleService.delete(uuid);
        return CustomApiResponse.ok("Role deleted successfully");
    }

    /**
     * Assigns authorities to a role.
     *
     * @param roleAuthoritiesDto the {@link RoleAuthoritiesDto} object containing the role and authority data.
     * @return {@link CustomApiResponse} containing the ID of the role and confirmation of assignment.
     */
    @PostMapping("/assign-authorities")
    public CustomApiResponse assignAuthorities(@Valid RoleAuthoritiesDto roleAuthoritiesDto) {
        RoleDto dto = roleService.assignAuthorities(roleAuthoritiesDto);
        return CustomApiResponse.ok("Role assigned successfully", dto.getId());
    }

}
