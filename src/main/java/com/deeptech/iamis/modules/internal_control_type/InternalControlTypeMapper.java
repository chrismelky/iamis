package com.deeptech.iamis.modules.internal_control_type;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link InternalControlType} entities and {@link InternalControlTypeDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface InternalControlTypeMapper {
  InternalControlType toEntity(InternalControlTypeDto internalControlTypeDto);

  InternalControlTypeDto toDto(InternalControlType internalControlType);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  InternalControlType partialUpdate(
    InternalControlTypeDto internalControlTypeDto,
    @MappingTarget InternalControlType internalControlType
  );
}
