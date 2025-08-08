package com.deeptech.iamis.modules.financial_year;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link FinancialYear} entities and {@link FinancialYearDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface FinancialYearMapper {
  FinancialYear toEntity(FinancialYearDto financialYearDto);

  FinancialYearDto toDto(FinancialYear financialYear);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  FinancialYear partialUpdate(
    FinancialYearDto financialYearDto,
    @MappingTarget FinancialYear financialYear
  );
}
