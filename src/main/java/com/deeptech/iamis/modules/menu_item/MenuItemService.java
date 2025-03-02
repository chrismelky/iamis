package com.deeptech.iamis.modules.menu_item;

import com.deeptech.iamis.core.BaseCrudService;
import com.deeptech.iamis.modules.menu_group.MenuGroupDto;

import java.util.List;
import java.util.Map;

public interface MenuItemService extends BaseCrudService<MenuItemDto, MenuItem> {

    List<MenuGroupDto> getWithItems(Map<String, List<Long>> groupItemIds);

    MenuItemDto assignAuthorities(MenuAuthoritiesDto menuAuthoritiesDto);

}
