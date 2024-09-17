package tz.go.zanemr.authorization_service.modules.user;

import org.springframework.data.jpa.repository.EntityGraph;
import tz.go.zanemr.authorization_service.modules.core.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles","roles.authorities"})
    Optional<User> findUserByEmail(String username);
}
