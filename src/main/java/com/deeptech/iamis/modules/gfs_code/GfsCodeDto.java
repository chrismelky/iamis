package com.deeptech.iamis.modules.gfs_code;

import com.deeptech.iamis.core.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the {@link GfsCode} entity.
 * This class is used to transfer GfsCode data between the client and server.
 * It extends {@link BaseDto}, which provides common fields like id, uuid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GfsCodeDto extends BaseDto {

  @NotNull
  @Size(max = 300)
  private String name;

  @NotNull
  @Size(max = 10)
  private String code;
}
