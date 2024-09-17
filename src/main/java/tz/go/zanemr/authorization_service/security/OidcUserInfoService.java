package tz.go.zanemr.authorization_service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import tz.go.zanemr.authorization_service.modules.core.Utils;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupDto;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupMapper;
import tz.go.zanemr.authorization_service.modules.menu_item.MenuItem;
import tz.go.zanemr.authorization_service.modules.menu_item.MenuItemRepository;
import tz.go.zanemr.authorization_service.modules.menu_item.MenuItemService;
import tz.go.zanemr.authorization_service.modules.user.User;
import tz.go.zanemr.authorization_service.modules.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcUserInfoService   {

    private final UserRepository userRepository;

    private final MenuItemRepository menuItemRepository;

    private final MenuItemService menuItemService;


    public OidcUserInfo loadUser(String username) {
        User user= userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // Get user menus
        return OidcUserInfo
                .builder()
                .subject(user.getEmail())
                .email(user.getEmail())
                .claim("menus", getMenus(user))
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .build();
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
        Map<String, List<Long>> groupItemIds = Utils.getMenuGroup(menuItems);
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
}

