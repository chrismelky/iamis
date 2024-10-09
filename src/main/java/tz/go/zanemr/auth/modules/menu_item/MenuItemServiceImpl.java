package tz.go.zanemr.auth.modules.menu_item;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.core.Utils;
import tz.go.zanemr.auth.modules.authority.AuthorityRepository;
import tz.go.zanemr.auth.core.SearchService;
import tz.go.zanemr.auth.modules.menu_group.MenuGroupDto;
import tz.go.zanemr.auth.modules.menu_group.MenuGroupMapper;
import tz.go.zanemr.auth.modules.menu_group.MenuGroupRepository;

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
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find menu item with uuid " + dto.getUuid()));
            menuItem = menuItemMapper.partialUpdate(dto, menuItem);
        } else {
            menuItem.setUuid(Utils.generateUuid());
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
                .orElseThrow(() -> new EntityNotFoundException("Cannot find menu item with uuid " + uuid));
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

    @Override
    public MenuItemDto assignAuthorities(MenuAuthoritiesDto menuAuthoritiesDto) {
        MenuItem menuItem = menuItemRepository.findByUuid(menuAuthoritiesDto.getMenuItemId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find menu item with uuid " + menuAuthoritiesDto.getMenuItemId()));

        menuItem.setAuthorities(new HashSet<>());
        for (UUID authorityId : menuAuthoritiesDto.getAuthorityIds()) {
            menuItem.addAuthority(authorityRepository.getReferenceByUuid(authorityId));
        }
        return menuItemMapper.toDto(menuItem);
    }
}
