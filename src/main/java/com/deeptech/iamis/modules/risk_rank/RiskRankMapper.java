package com.deeptech.iamis.modules.risk_rank;

import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link RiskRank} entities and {@link RiskRankDto} objects.
 * This interface uses MapStruct to automatically generate the mapping code at compile time.
 * It defines methods for full entity-to-DTO conversion, partial updates, and custom mappings.
 *<p>
 * The component model is set to Spring, allowing it to be used as a Spring bean.
 */
@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RiskRankMapper {
  RiskRank toEntity(RiskRankDto riskRankDto);

  RiskRankDto toDto(RiskRank riskRank);

  @BeanMapping(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
  )
  RiskRank partialUpdate(
    RiskRankDto riskRankDto,
    @MappingTarget RiskRank riskRank
  );
}
