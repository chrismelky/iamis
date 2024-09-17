package tz.go.zanemr.authorization_service.modules.menu_item;

import tz.go.zanemr.authorization_service.modules.core.BaseCrudService;
import tz.go.zanemr.authorization_service.modules.menu_group.MenuGroupDto;

import java.util.List;
import java.util.Map;

public interface MenuItemService extends BaseCrudService<MenuItemDto, MenuItem> {

    List<MenuGroupDto> getWithItems(Map<String, List<Long>> groupItemIds);

}
