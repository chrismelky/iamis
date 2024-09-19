package tz.go.zanemr.auth.modules.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tz.go.zanemr.auth.modules.authority.Authority;
import tz.go.zanemr.auth.modules.core.BaseModel;
import tz.go.zanemr.auth.modules.user.User;

import java.util.HashSet;
import java.util.Set;


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
