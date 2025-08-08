package com.deeptech.iamis.modules.category_of_finding;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link CategoryOfFinding} entities and {@link CategoryOfFindingDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CategoryOfFindingMapper {
  CategoryOfFinding toEntity(CategoryOfFindingDto categoryOfFindingDto);

  CategoryOfFindingDto toDto(CategoryOfFinding categoryOfFinding);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  CategoryOfFinding partialUpdate(
    CategoryOfFindingDto categoryOfFindingDto,
    @MappingTarget CategoryOfFinding categoryOfFinding
  );
}
