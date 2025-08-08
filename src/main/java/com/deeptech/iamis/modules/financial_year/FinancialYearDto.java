package com.deeptech.iamis.modules.financial_year;

import com.deeptech.iamis.core.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for the {@link FinancialYear} entity.
 * This class is used to transfer FinancialYear data between the client and server.
 * It extends {@link BaseDto}, which provides common fields like id, uuid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinancialYearDto extends BaseDto {

  @NotNull
  @Size(max = 100)
  private String name;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  @NotNull
  private Boolean active = Boolean.TRUE;
}
