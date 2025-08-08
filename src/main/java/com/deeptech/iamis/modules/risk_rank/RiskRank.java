package com.deeptech.iamis.modules.risk_rank;

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
 * Entity representing a RiskRank in the system.
 * This class maps to the 'risk_ranks' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "risk_ranks")
public class RiskRank extends BaseModel {

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
