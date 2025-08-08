package com.deeptech.iamis.modules.organisation_unit_level;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link OrganisationUnitLevel} entities and {@link OrganisationUnitLevelDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface OrganisationUnitLevelMapper {
  OrganisationUnitLevel toEntity(
    OrganisationUnitLevelDto organisationUnitLevelDto
  );

  OrganisationUnitLevelDto toDto(OrganisationUnitLevel organisationUnitLevel);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  OrganisationUnitLevel partialUpdate(
    OrganisationUnitLevelDto organisationUnitLevelDto,
    @MappingTarget OrganisationUnitLevel organisationUnitLevel
  );
}
