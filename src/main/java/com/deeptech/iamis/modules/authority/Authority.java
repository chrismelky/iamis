package com.deeptech.iamis.modules.authority;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.menu_item.MenuItem;
import com.deeptech.iamis.modules.role.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority extends BaseModel {

    @NotNull
    private String name;

    private String service;

    private String resource;

    private String action;

    private String method;

    private String description;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<MenuItem> menuItems = new HashSet<>();
}
