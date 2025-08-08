package com.deeptech.iamis.modules.organisation_unit;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link OrganisationUnit} entities and {@link OrganisationUnitDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface OrganisationUnitMapper {
  OrganisationUnit toEntity(OrganisationUnitDto organisationUnitDto);

  OrganisationUnitDto toDto(OrganisationUnit organisationUnit);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  OrganisationUnit partialUpdate(
    OrganisationUnitDto organisationUnitDto,
    @MappingTarget OrganisationUnit organisationUnit
  );
}
