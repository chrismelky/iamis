package com.deeptech.iamis.modules.risk_rank;

import com.deeptech.iamis.core.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the {@link RiskRank} entity.
 * This class is used to transfer RiskRank data between the client and server.
 * It extends {@link BaseDto}, which provides common fields like id, uuid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RiskRankDto extends BaseDto {

  @NotNull
  @Size(max = 100)
  private String name;

  @NotNull
  private Integer minValue;

  @NotNull
  private Integer maxValue;

  @Size(max = 10)
  private String color;
}
