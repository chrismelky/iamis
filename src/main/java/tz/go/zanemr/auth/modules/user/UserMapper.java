package tz.go.zanemr.auth.modules.user;

import org.mapstruct.*;
import tz.go.zanemr.auth.core.BaseModel;
import tz.go.zanemr.auth.modules.role.Role;

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