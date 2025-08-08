package com.deeptech.iamis.modules.gfs_code;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link GfsCode} entities and {@link GfsCodeDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface GfsCodeMapper {
  GfsCode toEntity(GfsCodeDto gfsCodeDto);

  GfsCodeDto toDto(GfsCode gfsCode);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  GfsCode partialUpdate(GfsCodeDto gfsCodeDto, @MappingTarget GfsCode gfsCode);
}
