package com.deeptech.iamis.modules.department;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link Department} entities and {@link DepartmentDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface DepartmentMapper {
  Department toEntity(DepartmentDto departmentDto);

  DepartmentDto toDto(Department department);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  Department partialUpdate(
    DepartmentDto departmentDto,
    @MappingTarget Department department
  );
}
