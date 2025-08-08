package com.deeptech.iamis.modules.organisation_unit_level;

import com.deeptech.iamis.core.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a OrganisationUnitLevel in the system.
 * This class maps to the 'organisation_unit_levels' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisation_unit_levels")
public class OrganisationUnitLevel extends BaseModel {

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
