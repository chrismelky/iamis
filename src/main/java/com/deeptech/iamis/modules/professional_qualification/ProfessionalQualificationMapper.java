package com.deeptech.iamis.modules.professional_qualification;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link ProfessionalQualification} entities and {@link ProfessionalQualificationDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProfessionalQualificationMapper {
  ProfessionalQualification toEntity(
    ProfessionalQualificationDto professionalQualificationDto
  );

  ProfessionalQualificationDto toDto(
    ProfessionalQualification professionalQualification
  );

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  ProfessionalQualification partialUpdate(
    ProfessionalQualificationDto professionalQualificationDto,
    @MappingTarget ProfessionalQualification professionalQualification
  );
}
