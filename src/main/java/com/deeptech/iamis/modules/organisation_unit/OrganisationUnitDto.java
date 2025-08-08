package com.deeptech.iamis.modules.organisation_unit;

import com.deeptech.iamis.core.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for the {@link OrganisationUnit} entity.
 * This class is used to transfer OrganisationUnit data between the client and server.
 * It extends {@link BaseDto}, which provides common fields like id, uuid.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganisationUnitDto extends BaseDto {

  @NotNull
  @Size(max = 100)
  private String name;

  @Size(max = 10)
  private String code;

  @Size(max = 12)
  private String phoneNumber;

  @Size(max = 50)
  private String email;

  private String address;
  private String background;
  private String letterHeadTitle;

  @NotNull
  private Long levelId;

  private String levelName;

  @NotNull
  private Long parentId;

  private String parentName;
}
