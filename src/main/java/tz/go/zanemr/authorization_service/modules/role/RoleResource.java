package tz.go.zanemr.authorization_service.modules.role;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tz.go.zanemr.authorization_service.modules.core.AppConstants;
import tz.go.zanemr.authorization_service.modules.core.CustomApiResponse;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX+"/roles")
public class RoleResource {

    private final RoleService roleService;

    @PostMapping
    public CustomApiResponse createRole(@Valid @RequestBody RoleDto dto) {
        if(dto.getId() != null) {
            throw new ValidationException("New role should not have an id");
        }
        return CustomApiResponse.ok(roleService.save(dto));
    }

    @PutMapping("/{uuid}")
    public CustomApiResponse updateRole(@Valid @RequestBody RoleDto dto,
                                        @PathVariable String uuid) {
        if(dto.getId() == null) {
            throw new ValidationException("Update role should have an id");
        }
        if(!uuid.equals(dto.getUuid())) {
            throw new ValidationException("uuid should be equal to role uuid");
        }
       return CustomApiResponse.ok(roleService.save(dto));
    }

    @GetMapping
    public CustomApiResponse getRoles(Pageable pageable, Map<String, Object> searchParams) {
        return CustomApiResponse.ok(roleService.findAll(pageable, searchParams));
    }

    @DeleteMapping("/{uuid}")
    public CustomApiResponse deleteRole(@PathVariable UUID uuid) {
        roleService.delete(uuid);
        return CustomApiResponse.ok("Role deleted successfully");
    }

    @PostMapping("/assign-authorities")
    public CustomApiResponse assignAuthorities(@Valid RoleAuthorities roleAuthorities) {
        RoleDto dto = roleService.assignAuthorities(roleAuthorities);
        return CustomApiResponse.ok("Role assigned successfully", dto.getId());
    }

}
