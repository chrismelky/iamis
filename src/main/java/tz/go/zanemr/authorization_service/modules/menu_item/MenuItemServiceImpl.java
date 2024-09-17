package tz.go.zanemr.authorization_service.modules.menu_item;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tz.go.zanemr.authorization_service.modules.authority.AuthorityRepository;
import tz.go.zanemr.authorization_service.modules.core.SearchService;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupDto;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupMapper;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuItemServiceImpl extends SearchService<MenuItem> implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    private final MenuGroupMapper menuGroupMapper;

    private final AuthorityRepository authorityRepository;

    private final MenuGroupRepository menuGroupRepository;

    @Override
    public MenuItemDto save(MenuItemDto dto) {
        MenuItem menuItem = menuItemMapper.toEntity(dto);
        if (dto.getUuid() != null) {
            menuItem = menuItemRepository.findByUuid(dto.getUuid())
                    .orElseThrow(() -> new ValidationException("Cannot find menu item with uuid " + dto.getUuid()));
            menuItem = menuItemMapper.partialUpdate(dto, menuItem);
            menuItem.setAuthorities(new HashSet<>());
        }
        if (dto.getAuthorityIds() != null) {
            for (UUID authorityId : dto.getAuthorityIds()) {
                menuItem.addAuthority(authorityRepository.getReferenceByUuid(authorityId));
            }
        }
        menuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public Page<MenuItemDto> findAll(Pageable pageable, Map<String, Object> search) {
        return menuItemRepository.findAll(createSpecification(MenuItem.class, search), pageable)
                .map(menuItemMapper::toDto);
    }

    @Override
    public MenuItemDto findById(UUID uuid) {
        return menuItemRepository.findByUuid(uuid)
                .map(menuItemMapper::toDto)
                .orElseThrow(() -> new ValidationException("Cannot find menu item with uuid " + uuid));
    }

    @Override
    public void delete(UUID uuid) {
        try {
            menuItemRepository.deleteByUuid(uuid);
        } catch (Exception e) {
            throw new ValidationException("Cannot delete menu item with uuid " + uuid);
        }

    }

    @Override
    public List<MenuGroupDto> getWithItems(Map<String, List<Long>> groupItemIds) {
        List<MenuGroupDto> groups = menuGroupRepository.byIds(
                        groupItemIds.get("groupIds"))
                .stream().map(menuGroupMapper::toDto).toList();
        groups.forEach(
                g -> {
                    g.setChildren(menuItemRepository.byGroupAndIds(g.getId(), groupItemIds.get("itemIds"))
                            .stream().map(menuItemMapper::toDtoNoAuth).toList()
                    );
                });
        return groups;
    }
}
