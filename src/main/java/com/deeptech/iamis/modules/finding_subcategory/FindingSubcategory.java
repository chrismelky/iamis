package com.deeptech.iamis.modules.finding_subcategory;

import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.finding_category.FindingCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a FindingSubcategory in the system.
 * This class maps to the 'finding_subcategories' table in the database.
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', uuid, 'createdAt', createdBy etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "finding_subcategories")
public class FindingSubcategory extends BaseModel {

  @NotNull
  @Size(max = 100)
  private String name;

  @NotNull
  @Size(max = 10)
  private String code;

  @NotNull
  @Column(name = "finding_category_id")
  private Long findingCategoryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({ "findingSubcategories" })
  @JoinColumn(
    name = "finding_category_id",
    insertable = false,
    updatable = false
  )
  private FindingCategory findingCategory;
}
