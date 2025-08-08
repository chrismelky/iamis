package com.deeptech.iamis.modules.organisation_unit_level;

import com.deeptech.iamis.core.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the {@link OrganisationUnitLevel} entity.
 * This class is used to transfer OrganisationUnitLevel data between the client and server.
 * It extends {@link BaseDto}, which provides common fields like id, uuid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganisationUnitLevelDto extends BaseDto {

  @NotNull
  @Size(max = 100)
  private String name;

  @Size(max = 10)
  private String code;

  @NotNull
  private Integer position;

  private Boolean isAuditableLevel = Boolean.TRUE;
  private Boolean isRiskOwnerLevel = Boolean.TRUE;
  private Boolean isRiskRegisterLevel = Boolean.TRUE;
}
