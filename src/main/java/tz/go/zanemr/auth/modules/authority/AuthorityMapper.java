package tz.go.zanemr.auth.modules.authority;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorityMapper {
    Authority toEntity(AuthorityDto authorityDto);

    AuthorityDto toDto(Authority authority);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Authority partialUpdate(AuthorityDto authorityDto, @MappingTarget Authority authority);
}