package tz.go.zanemr.auth.modules.role;

import org.mapstruct.*;
import tz.go.zanemr.auth.modules.authority.Authority;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    Role toEntity(RoleDto roleDto);

    @Mapping(source = "authorities", target = "authorityIds", qualifiedByName = "authorityToIds")
    RoleDto toDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleDto roleDto, @MappingTarget Role role);

    @Named("authorityToIds")
    default List<UUID> authorityToIds(Set<Authority> authorities) {
        if(authorities == null) {
            return null;
        }
        return authorities.stream().map(Authority::getUuid).toList();
    }
}