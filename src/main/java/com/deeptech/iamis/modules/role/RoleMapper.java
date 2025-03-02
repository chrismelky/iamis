package com.deeptech.iamis.modules.role;

import org.mapstruct.*;
import com.deeptech.iamis.modules.authority.Authority;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Mapper interface for converting between {@link Role} entities and {@link RoleDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
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