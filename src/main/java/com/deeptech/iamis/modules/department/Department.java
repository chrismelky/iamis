package com.deeptech.iamis.modules.department;

import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.organisation_unit.OrganisationUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a Department in the system.
 * This class maps to the 'departments' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
public class Department extends BaseModel {

  @NotNull
  @Size(max = 100)
  private String name;

  @Size(max = 10)
  private String code;

  @NotNull
  @Column(name = "organisation_unit_id")
  private Long organisationUnitId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({ "departments" })
  @JoinColumn(
    name = "organisation_unit_id",
    insertable = false,
    updatable = false
  )
  private OrganisationUnit organisationUnit;
}
