package com.deeptech.iamis.modules.menu_group;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.deeptech.iamis.core.AppConstants;
import com.deeptech.iamis.core.CustomApiResponse;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/menu-groups")
public class MenuGroupResource {

    private final MenuGroupService menuGroupService;

    @GetMapping
    public CustomApiResponse get(Pageable pageable,@RequestParam Map<String, Object> searchParams) {
        return CustomApiResponse.ok(menuGroupService.findAll(pageable, searchParams));
    }

    @PostMapping
    public CustomApiResponse create(@Valid @RequestBody MenuGroupDto dto) {
        if (dto.getId() != null) {
            throw new ValidationException("New menu group should not have an id");
        }
        return CustomApiResponse.ok("Menu group created", menuGroupService.save(dto));
    }

    @PutMapping("/{uuid}")
    public CustomApiResponse update(@Valid @RequestBody MenuGroupDto dto,
                                    @PathVariable("uuid") UUID uuid) {
        if(dto.getUuid() == null){
            throw new ValidationException("Menu group uuid should not be null");
        }
        if(!uuid.equals(dto.getUuid())){
            throw new ValidationException("Menu group uuid does not match");
        }
        return CustomApiResponse.ok("Menu group updated", menuGroupService.save(dto));
    }

    @GetMapping("/{uuid}")
    public CustomApiResponse findById(@PathVariable("uuid") UUID uuid) {
        return CustomApiResponse.ok("Menu group found", menuGroupService.findById(uuid));
    }

    @DeleteMapping("/{uuid}")
    public CustomApiResponse delete(@PathVariable("uuid") UUID uuid) {
        menuGroupService.delete(uuid);
        return CustomApiResponse.ok("Menu group deleted");
    }
}
