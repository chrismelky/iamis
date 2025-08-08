package com.deeptech.iamis.modules.organisation_unit;

import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.organisation_unit_level.OrganisationUnitLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a OrganisationUnit in the system.
 * This class maps to the 'organisation_units' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisation_units")
public class OrganisationUnit extends BaseModel {

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
  @Column(name = "level_id")
  private Long levelId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({ "organisationUnits" })
  @JoinColumn(name = "level_id", insertable = false, updatable = false)
  private OrganisationUnitLevel level;

  @NotNull
  @Column(name = "parent_id")
  private Long parentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({ "organisationUnits" })
  @JoinColumn(name = "parent_id", insertable = false, updatable = false)
  private OrganisationUnit parent;
}
