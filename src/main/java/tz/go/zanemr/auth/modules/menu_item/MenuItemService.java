package tz.go.zanemr.auth.modules.menu_item;

import tz.go.zanemr.auth.modules.core.BaseCrudService;
import tz.go.zanemr.auth.modules.menu_group.MenuGroupDto;

import java.util.List;
import java.util.Map;

public interface MenuItemService extends BaseCrudService<MenuItemDto, MenuItem> {

    List<MenuGroupDto> getWithItems(Map<String, List<Long>> groupItemIds);

}
