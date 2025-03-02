package com.deeptech.iamis.authentication;

import com.deeptech.iamis.core.CustomApiResponse;
import com.deeptech.iamis.modules.menu_group.MenuGroup;
import com.deeptech.iamis.modules.menu_group.MenuGroupDto;
import com.deeptech.iamis.modules.menu_group.MenuGroupRepository;
import com.deeptech.iamis.modules.menu_item.MenuItem;
import com.deeptech.iamis.modules.menu_item.MenuItemRepository;
import com.deeptech.iamis.modules.menu_item.MenuItemService;
import com.deeptech.iamis.modules.role.Role;
import com.deeptech.iamis.modules.role.RoleRepository;
import com.deeptech.iamis.modules.user.User;
import com.deeptech.iamis.modules.user.UserMapper;
import com.deeptech.iamis.modules.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.deeptech.iamis.security.SecurityUtils.AUTHORITIES_KEY;
import static com.deeptech.iamis.security.SecurityUtils.JWT_ALGORITHM;

/**
 * Controller to authenticate users.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticateController {

    private final JwtEncoder jwtEncoder;

    private final UserRepository userRepository;

    private final MenuItemService menuItemService;

    private final MenuItemRepository menuItemRepository;

    private final UserMapper userMapper;

    @Value("${security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/authenticate")
    public CustomApiResponse authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);

        User user = userRepository.findUserByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));

        Map<String, Object> body = new HashMap<>();
        body.put("token", jwt);
        body.put("user", userMapper.toDto(user));
        body.put("authorities", getAuthorities(user));
        body.put("menus", getMenus(user));

        return CustomApiResponse.ok(body);
    }

    /**
     * {@code GET /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param principal the authentication principal.
     * @return the login if the user is authenticated.
     */
    @GetMapping(value = "/authenticate", produces = MediaType.TEXT_PLAIN_VALUE)
    public String isAuthenticated(Principal principal) {
        log.debug("REST request to check if the current user is authenticated");
        return principal == null ? null : principal.getName();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
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
