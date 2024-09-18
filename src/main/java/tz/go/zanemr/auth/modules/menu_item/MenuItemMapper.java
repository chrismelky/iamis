package tz.go.zanemr.auth.modules.menu_item;

import org.mapstruct.*;
import tz.go.zanemr.auth.modules.authority.Authority;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuItemMapper {

    MenuItem toEntity(MenuItemDto menuItemDto);

    @Mapping(source = "authorities", target = "authorityIds", qualifiedByName = "authorityToIds")
    MenuItemDto toDto(MenuItem menuItem);

    MenuItemDto toDtoNoAuth(MenuItem menuItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MenuItem partialUpdate(MenuItemDto menuItemDto, @MappingTarget MenuItem menuItem);

    @Named("authorityToIds")
    default List<UUID> authorityToIds(Set<Authority> authoritySet) {
        if(authoritySet == null) {
            return null;
        }
       return authoritySet.stream().map(Authority::getUuid).toList();
    }
}