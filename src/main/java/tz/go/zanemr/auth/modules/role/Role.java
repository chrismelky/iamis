package tz.go.zanemr.auth.modules.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tz.go.zanemr.auth.modules.authority.Authority;
import tz.go.zanemr.auth.core.BaseModel;
import tz.go.zanemr.auth.modules.user.User;

import java.util.HashSet;
import java.util.Set;


/**
 * Entity representing a Role in the system. A Role can be assigned to multiple users
 * and can have multiple authorities. This class maps to the 'roles' table in the database.
 * <p>
 * - Role-to-User: Many-to-Many relationship
 * - Role-to-Authority: Many-to-Many relationship
 * <p>
 * Inherits from {@link BaseModel}, which includes common auditing fields like 'id', 'createdAt', etc.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseModel {

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "code", unique = true)
  private String code;

  @JsonIgnore
  @ManyToMany(mappedBy = "roles")
  @JsonIgnoreProperties({"roles"})
  private Set<User> users = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable(
          name = "role_authorities",
          joinColumns = {@JoinColumn(name = "role_id")},
          inverseJoinColumns = {@JoinColumn(name = "authority_id")})
  private Set<Authority> authorities = new HashSet<>();

  public void addAuthority(Authority authority) {
    authorities.add(authority);
    authority.getRoles().add(this);
  }

}
