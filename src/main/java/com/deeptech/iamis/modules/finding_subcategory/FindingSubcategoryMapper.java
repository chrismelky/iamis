package com.deeptech.iamis.modules.finding_subcategory;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link FindingSubcategory} entities and {@link FindingSubcategoryDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface FindingSubcategoryMapper {
  FindingSubcategory toEntity(FindingSubcategoryDto findingSubcategoryDto);

  FindingSubcategoryDto toDto(FindingSubcategory findingSubcategory);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  FindingSubcategory partialUpdate(
    FindingSubcategoryDto findingSubcategoryDto,
    @MappingTarget FindingSubcategory findingSubcategory
  );
}
