package tz.go.zanemr.authorization_service.modules.authority;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.zanemr.authorization_service.modules.core.BaseModel;
import tz.go.zanemr.authorization_service.modules.menu_item.MenuItem;
import tz.go.zanemr.authorization_service.modules.role.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority extends BaseModel {

    private String name;

    private String description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<MenuItem> menuItems = new HashSet<>();
}
