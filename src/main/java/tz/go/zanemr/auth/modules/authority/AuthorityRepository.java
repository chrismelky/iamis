package tz.go.zanemr.auth.modules.authority;

import jakarta.validation.constraints.NotNull;
import tz.go.zanemr.auth.core.BaseRepository;

import java.util.Optional;

public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    Boolean existsByName(@NotNull String name);
}