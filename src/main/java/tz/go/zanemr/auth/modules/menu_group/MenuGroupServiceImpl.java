package tz.go.zanemr.auth.modules.menu_group;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.core.SearchService;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuGroupServiceImpl extends SearchService<MenuGroup> implements MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    private final MenuGroupMapper menuGroupMapper;

    @Override
    public MenuGroupDto save(MenuGroupDto dto) {
        MenuGroup menuGroup = menuGroupMapper.toEntity(dto);
        if (dto.getUuid() != null) {
            menuGroup = menuGroupRepository.findByUuid(dto.getUuid())
                    .orElseThrow(() -> new RuntimeException("Menu group not found"));
            menuGroup = menuGroupMapper.partialUpdate(dto, menuGroup);
        }
        menuGroup = menuGroupRepository.save(menuGroup);
        return menuGroupMapper.toDto(menuGroup);
    }

    @Override
    public Page<MenuGroupDto> findAll(Pageable pageable, Map<String, Object> search) {

        return menuGroupRepository.findAll(
                        createSpecification(MenuGroup.class, search), pageable)
                .map(menuGroupMapper::toDto);
    }

    @Override
    public MenuGroupDto findById(UUID uuid) {
        return menuGroupRepository.findByUuid(uuid).map(menuGroupMapper::toDto)
                .orElseThrow(() -> new ValidationException("Menu group not found"));
    }

    @Override
    public void delete(UUID uuid) {
        try {
            menuGroupRepository.deleteByUuid(uuid);
        }catch (Exception e) {
            throw new ValidationException("Cannot delete menu group");
        }
    }
}
