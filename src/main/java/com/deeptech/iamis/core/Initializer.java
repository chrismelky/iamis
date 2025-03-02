package com.deeptech.iamis.core;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import com.deeptech.iamis.modules.authority.Authority;
import com.deeptech.iamis.modules.authority.AuthorityRepository;
import com.deeptech.iamis.modules.menu_group.MenuGroup;
import com.deeptech.iamis.modules.menu_group.MenuGroupRepository;
import com.deeptech.iamis.modules.menu_item.MenuItem;
import com.deeptech.iamis.modules.menu_item.MenuItemRepository;
import com.deeptech.iamis.modules.role.Role;
import com.deeptech.iamis.modules.role.RoleRepository;
import com.deeptech.iamis.modules.user.User;
import com.deeptech.iamis.modules.user.UserRepository;

import java.util.*;

import static com.deeptech.iamis.core.Utils.splitCamelCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder bcryptEncoder;

    private final MenuGroupRepository menuGroupRepository;

    private final MenuItemRepository menuItemRepository;

    @Value("${admin-email}")
    private String adminEmail;

    @Value("${admin-password}")
    private String adminPassword;


    // Load authorities
    public void initializeAuthorities() {
        try {
            requestMappingHandlerMapping
                    .getHandlerMethods()
                    .forEach(
                            (requestMappingInfo, handlerMethod) -> {
                                NoAuthorization noAuthorization =
                                        handlerMethod.getMethodAnnotation(NoAuthorization.class);

                                if (!requestMappingInfo.getPatternValues().isEmpty()) {
                                    List<String> urls = new ArrayList<>(requestMappingInfo.getPatternValues());

                                    if (!requestMappingInfo.getMethodsCondition().getMethods().isEmpty()
                                            && urls.size() == 1
                                            && urls.getFirst().contains("api")
                                            && noAuthorization == null) {

                                        String resourceName =
                                                handlerMethod.getBean().toString().replace("Resource", "");
                                        resourceName =
                                                resourceName.substring(0, 1).toUpperCase() + resourceName.substring(1);
                                        String actionName = handlerMethod.getMethod().getName();
                                        String methodName = null;
                                        if (!requestMappingInfo.getMethodsCondition().getMethods().isEmpty()) {
                                            methodName = requestMappingInfo.getMethodsCondition().getMethods().toString();
                                        }
                                        String actionDisplayName = splitCamelCase(actionName);
                                        String authName = resourceName.toUpperCase().concat("_").concat(actionName.toUpperCase());

                                        actionDisplayName =
                                                actionDisplayName.substring(0, 1).toUpperCase()
                                                        + actionDisplayName.substring(1);
                                        if (!authorityRepository.existsByResourceAndAction(resourceName, actionName)) {
                                            Authority authority = new Authority();
                                            authority.setName(authName);
                                            authority.setAction(actionName);
                                            authority.setResource(resourceName);
                                            authority.setMethod(methodName);
                                            authority.setDescription(actionDisplayName);
                                            authorityRepository.save(authority);
                                        }
                                    }
                                }
                            });
        } catch (Exception e) {
            log.error("############# Error on initializing authorities #####################", e);
            System.exit(1);
        }
    }

    private Role initializeRole() {
        Optional<Role> roleOptional = roleRepository.findRoleByCode("SUPER_ADMINISTRATOR");
        if (roleOptional.isEmpty()) {
            Set<Authority> authorities = new HashSet<>(authorityRepository.findAll());
            Role role = new Role();
            role.setName("SUPER ADMINISTRATOR");
            role.setCode(String.join("_", role.getName().split(" ")));
            authorities.forEach(role::addAuthority);
            log.info("The role with name SUPER ADMINISTRATOR has been created successfully");
            return roleRepository.save(role);
        }
        return roleOptional.get();
    }

    private void initializeUsers(Role role) {
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(adminEmail);
            if (optionalUser.isEmpty()) {
                User user = new User();
                user.setFirstName("Super Admin");
                user.setEmail(adminEmail);
                user.setPassword(bcryptEncoder.encode(adminPassword));
                user.setIsActive(true);
                user.getRoles().add(role);
                userRepository.save(user);
                log.info("The user with name username Super admin  has been created successfully");
            }
        } catch (Exception e) {
            log.error("############# Error on initializing users #####################", e);
            System.exit(1);
        }
    }

    public void initializeMenus() {
        if (menuGroupRepository.count() == 0) {
            MenuGroup group = new MenuGroup();
            group.setName("System Configuration");
            group.setIcon("pi pi-cog");
            group.setSortOrder(2);

            menuGroupRepository.save(group);

            MenuItem roleMenu = new MenuItem();
            roleMenu.setMenuGroup(group);
            roleMenu.setMenuGroupId(group.getId());
            roleMenu.setName("Roles");
            roleMenu.setIcon("pi pi-users");
            roleMenu.setState("/main/config/role");
            roleMenu.setSortOrder(1);
            Set<Authority> roleAuth = authorityRepository.findAllByResourceAndAction("Role", "create");
            roleAuth.forEach(roleMenu::addAuthority);
            menuItemRepository.save(roleMenu);

            MenuItem groupMenu = new MenuItem();
            groupMenu.setMenuGroup(group);
            groupMenu.setMenuGroupId(group.getId());
            groupMenu.setName("Menu Groups");
            groupMenu.setIcon("pi pi-expand");
            groupMenu.setState("/main/config/menu-group");
            groupMenu.setSortOrder(2);
            Set<Authority> groupAuth = authorityRepository.findAllByResourceAndAction("MenuGroup", "create");
            groupAuth.forEach(groupMenu::addAuthority);
            menuItemRepository.save(groupMenu);

            MenuItem itemMenu = new MenuItem();
            itemMenu.setMenuGroup(group);
            itemMenu.setMenuGroupId(group.getId());
            itemMenu.setName("Menu Items");
            itemMenu.setIcon("pi pi-bars");
            itemMenu.setState("/main/config/menu-item");
            itemMenu.setSortOrder(3);
            Set<Authority> itemAuth = authorityRepository.findAllByResourceAndAction("MenuItem", "create");
            itemAuth.forEach(itemMenu::addAuthority);
            menuItemRepository.save(itemMenu);

            MenuItem usersMenu = new MenuItem();
            usersMenu.setMenuGroup(group);
            usersMenu.setMenuGroupId(group.getId());
            usersMenu.setName("Users");
            groupMenu.setIcon("pi pi-user");
            usersMenu.setState("/main/config/user");
            usersMenu.setSortOrder(4);
            Set<Authority> usersAuth = authorityRepository.findAllByResourceAndAction("User", "create");
            usersAuth.forEach(usersMenu::addAuthority);
            menuItemRepository.save(usersMenu);
        }
    }

    public void init() {
        initializeAuthorities();
        Role role = initializeRole();
        initializeUsers(role);
        initializeMenus();
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        init();
    }
}
