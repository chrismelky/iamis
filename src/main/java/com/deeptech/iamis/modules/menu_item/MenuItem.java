package com.deeptech.iamis.modules.menu_item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.deeptech.iamis.modules.authority.Authority;
import com.deeptech.iamis.core.BaseModel;
import com.deeptech.iamis.modules.menu_group.MenuGroup;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_items")
public class MenuItem extends BaseModel {

    @NotNull
    private String name;

    private String icon;

    @NotNull
    private String state;

    @NotNull
    private Integer sortOrder;

    private String translationLabel;

    @NotNull
    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", insertable = false, updatable = false)
    private MenuGroup menuGroup;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "menu_item_authorities",
            joinColumns = {@JoinColumn(name = "menu_item_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    @JsonIgnoreProperties({"roles", "menuItems"})
    @JsonIgnore
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.getMenuItems().add(this);
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
        authority.getMenuItems().remove(this);
    }
}
