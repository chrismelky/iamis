package com.deeptech.iamis.modules.authority;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import com.deeptech.iamis.core.BaseRepository;

import java.util.List;
import java.util.Set;

public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    Boolean existsByName(@NotNull String name);

    boolean existsByResourceAndAction(String resourceName, String actionName);

    Set<Authority> findAllByResourceAndAction(String role, String create);

    @Query("Select a from Authority a order by a.resource, a.method")
    List<Authority> findAll();
}