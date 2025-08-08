package com.deeptech.iamis.modules.finding_category;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link FindingCategory} entities and {@link FindingCategoryDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface FindingCategoryMapper {
  FindingCategory toEntity(FindingCategoryDto findingCategoryDto);

  FindingCategoryDto toDto(FindingCategory findingCategory);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  FindingCategory partialUpdate(
    FindingCategoryDto findingCategoryDto,
    @MappingTarget FindingCategory findingCategory
  );
}
