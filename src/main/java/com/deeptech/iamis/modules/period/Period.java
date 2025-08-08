package com.deeptech.iamis.modules.period;

import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.financial_year.FinancialYear;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity representing a Period in the system.
 * This class maps to the 'periods' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "periods")
public class Period extends BaseModel {

  @NotNull
  @Size(max = 100)
  private String name;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  @NotNull
  private Boolean active = Boolean.TRUE;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PeriodType periodType;

  @NotNull
  @Column(name = "financial_year_id")
  private Long financialYearId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({ "periods" })
  @JoinColumn(name = "financial_year_id", insertable = false, updatable = false)
  private FinancialYear financialYear;
}
