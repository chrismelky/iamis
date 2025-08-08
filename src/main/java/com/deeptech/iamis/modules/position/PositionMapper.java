package com.deeptech.iamis.modules.position;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link Position} entities and {@link PositionDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface PositionMapper {
  Position toEntity(PositionDto positionDto);

  PositionDto toDto(Position position);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  Position partialUpdate(
    PositionDto positionDto,
    @MappingTarget Position position
  );
}
