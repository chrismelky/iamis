package tz.go.zanemr.authorization_service.modules.core;

import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroup;
import tz.go.zanemr.authorization_service.modules.menu_item.MenuItem;

import java.util.*;

public class Utils {

    public static Map<String, List<Long>> getMenuGroup(Set<MenuItem> menuItems) {

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
    private static void getGroup(MenuGroup group, List<Long> collect) {
        collect.add(group.getId());
    }
}
