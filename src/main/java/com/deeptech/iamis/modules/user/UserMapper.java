package com.deeptech.iamis.modules.user;

import org.mapstruct.*;
import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.role.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserDto userDto);

    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "rolesToIds")
    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    @Named("rolesToIds")
    default List<UUID> rolesToIds(Set<Role> roles) {
        if(roles == null) return null;
        return roles.stream().map(BaseModel::getUuid).toList();
    }
}