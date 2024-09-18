package tz.go.zanemr.auth.modules.menu_group;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuGroupMapper {
    MenuGroup toEntity(MenuGroupDto menuGroupDto);

    MenuGroupDto toDto(MenuGroup menuGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MenuGroup partialUpdate(MenuGroupDto menuGroupDto, @MappingTarget MenuGroup menuGroup);
}