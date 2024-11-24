package tz.go.zanemr.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.modules.menu_group.MenuGroup;
import tz.go.zanemr.auth.modules.menu_group.MenuGroupDto;
import tz.go.zanemr.auth.modules.menu_item.MenuItem;
import tz.go.zanemr.auth.modules.menu_item.MenuItemRepository;
import tz.go.zanemr.auth.modules.menu_item.MenuItemService;
import tz.go.zanemr.auth.modules.role.Role;
import tz.go.zanemr.auth.modules.user.User;
import tz.go.zanemr.auth.modules.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final UserRepository userRepository;

    private final MenuItemRepository menuItemRepository;

    private final MenuItemService menuItemService;


    public OidcUserInfo loadUser(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // Get user menus
        return OidcUserInfo
                .builder()
                .subject(user.getEmail())
                .email(user.getEmail())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("middleName", user.getLastName())
                .claim("lastName", user.getLastName())
                .claim("passwordChanged", user.getPasswordChanged())
                .claim("facilityName", user.getFacilityName())
                .claim("facilityId", user.getFacilityId())
                .claim("facilityUuid", user.getFacilityUuid() != null ? user.getFacilityUuid().toString() : null)
                .claim("facilityCode", user.getFacilityCode())
                .claim("isActive", user.getIsActive())
                .claim("menus", getMenus(user))
                .claim("authorities", getAuthorities(user))
                .build();
    }

    public List<String> getAuthorities(User user) {
        List<String> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            role.getAuthorities()
                    .forEach(authority ->
                            authorities.add(authority.getName()));
        }
        return authorities;
    }


    private List<MenuGroupDto> getMenus(User user) {
        List<Long> authIds = new ArrayList<>();
        authIds.add(0L);
        user.getRoles()
                .forEach(
                        r ->
                                r.getAuthorities()
                                        .forEach(
                                                a -> {
                                                    authIds.add(a.getId());
                                                }));
        log.info("Auth ids {} ", authIds);
        log.info("Find menu items with groups by auth ids {} ", authIds);
        Set<MenuItem> menuItems = menuItemRepository.findByAuthorities(authIds);
        log.info(
                "menu items founds {} ",
                menuItems.stream().map(MenuItem::getName).collect(Collectors.toSet()));
        log.info("Find menu items with no groups by auth ids {} ", authIds);
        Set<MenuItem> menuItems2 = menuItemRepository.findWithNoGroupByAuthorities(authIds);
        log.info(
                "menu items with no group founds {} ",
                menuItems2.stream().map(MenuItem::getName).collect(Collectors.toSet()));
        Map<String, List<Long>> groupItemIds = getMenuGroup(menuItems);
        log.info("group and iterm ids {} ", groupItemIds);

        List<MenuGroupDto> itemAsGroup =
                menuItems2.stream()
                        .map(
                                i ->
                                        new MenuGroupDto(
                                                i.getId(),
                                                i.getUuid(),
                                                i.getName(),
                                                i.getName(),
                                                i.getState(),
                                                i.getIcon(),
                                                i.getSortOrder()))
                        .collect(Collectors.toList());
        log.info("get items and group filtered by corresponding ids");
        itemAsGroup.addAll(menuItemService.getWithItems(groupItemIds));
        log.info("Sorting groups");

        itemAsGroup.sort(Comparator.comparingInt(o -> (o.getSortOrder() != null ? o.getSortOrder() : 0)));

        return itemAsGroup;
    }

    public Map<String, List<Long>> getMenuGroup(Set<MenuItem> menuItems) {

        Map<String, List<Long>> groupItemIds = new HashMap<>();
        List<Long> menuGroupIds = new ArrayList<>();
        List<Long> menuItemsIds = new ArrayList<>();
        menuGroupIds.add(0L);
        menuItemsIds.add(0L);

        menuItems.forEach(
                m -> {
                    menuItemsIds.add(m.getId());
                    if (m.getMenuGroup() != null) {
                        getGroup(m.getMenuGroup(), menuGroupIds);
                    }
                });
        groupItemIds.put("groupIds", menuGroupIds);
        groupItemIds.put("itemIds", menuItemsIds);
        return groupItemIds;
    }

    private void getGroup(MenuGroup group, List<Long> collect) {
        collect.add(group.getId());
    }

}

