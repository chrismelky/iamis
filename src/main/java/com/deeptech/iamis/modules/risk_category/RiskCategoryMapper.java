package com.deeptech.iamis.modules.risk_category;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link RiskCategory} entities and {@link RiskCategoryDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RiskCategoryMapper {
  RiskCategory toEntity(RiskCategoryDto riskCategoryDto);

  RiskCategoryDto toDto(RiskCategory riskCategory);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  RiskCategory partialUpdate(
    RiskCategoryDto riskCategoryDto,
    @MappingTarget RiskCategory riskCategory
  );
}
