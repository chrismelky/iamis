package tz.go.zanemr.auth.modules.menu_item;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tz.go.zanemr.auth.core.AppConstants;
import tz.go.zanemr.auth.core.CustomApiResponse;
import tz.go.zanemr.auth.modules.role.RoleAuthoritiesDto;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/menu-items")
public class MenuItemResource {

    private final MenuItemService menuItemService;

    @GetMapping
    public CustomApiResponse get(Pageable pageable,
                                 @RequestParam Map<String, Object> searchParams) {
        return CustomApiResponse.ok(menuItemService.findAll(pageable, searchParams));
    }

    @PostMapping
    public CustomApiResponse create(@Valid @RequestBody MenuItemDto dto) {
        if (dto.getUuid() != null || dto.getId() != null) {
            throw new ValidationException("New menu item should not have a id or uuid");
        }
        return CustomApiResponse.ok(menuItemService.save(dto));
    }

    @PutMapping("/{uuid}")
    public CustomApiResponse update(@Valid @RequestBody MenuItemDto dto, @PathVariable UUID uuid) {
        if (dto.getUuid() == null || dto.getId() == null) {
            throw new ValidationException("Updated menu item should  have an id and uuid");
        }
        if (!uuid.equals(dto.getUuid())) {
            throw new ValidationException("Uuid mismatch");
        }
        return CustomApiResponse.ok(menuItemService.save(dto));
    }

    @GetMapping("/{uuid}")
    public CustomApiResponse findById(@PathVariable UUID uuid) {
        return CustomApiResponse.ok(menuItemService.findById(uuid));
    }

    @DeleteMapping("/{uuid}")
    public CustomApiResponse delete(@PathVariable UUID uuid) {
        menuItemService.delete(uuid);
        return CustomApiResponse.ok("Menu item deleted");
    }

    /**
     * Assigns authorities to a menu item.
     *
     * @param menuAuthoritiesDto the {@link RoleAuthoritiesDto} object containing the menuitem and authority data.
     * @return {@link CustomApiResponse} containing the ID of the menuitem and confirmation of assignment.
     */
    @PostMapping("/assign-authorities")
    public CustomApiResponse assignAuthorities(@Valid MenuAuthoritiesDto menuAuthoritiesDto) {
        MenuItemDto dto = menuItemService.assignAuthorities(menuAuthoritiesDto);
        return CustomApiResponse.ok("MenuItem assigned successfully", dto.getId());
    }
}
