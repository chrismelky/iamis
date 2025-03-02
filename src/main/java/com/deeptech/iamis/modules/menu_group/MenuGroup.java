package com.deeptech.iamis.modules.menu_group;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.deeptech.iamis.core.BaseModel;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_groups")
public class MenuGroup extends BaseModel {

    @NotNull
    private String name;

    private String icon;

    @NotNull
    private Integer sortOrder;
}
