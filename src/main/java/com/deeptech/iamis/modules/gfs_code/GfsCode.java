package com.deeptech.iamis.modules.gfs_code;

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
 * Entity representing a GfsCode in the system.
 * This class maps to the 'gfs_codes' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gfs_codes")
public class GfsCode extends BaseModel {

  @NotNull
  @Size(max = 300)
  private String name;

  @NotNull
  @Size(max = 10)
  private String code;
}
