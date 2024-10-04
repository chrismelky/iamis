package tz.go.zanemr.auth.modules.authority;

import jakarta.validation.constraints.NotNull;
import tz.go.zanemr.auth.core.BaseRepository;

import java.util.Set;

public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    Boolean existsByName(@NotNull String name);

    boolean existsByResourceAndAction(String resourceName, String actionName);

    Set<Authority> findByService(String serviceName);

    Set<Authority> findAllByResourceAndAction(String role, String create);
}