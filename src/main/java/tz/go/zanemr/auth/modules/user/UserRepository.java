package tz.go.zanemr.auth.modules.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tz.go.zanemr.auth.core.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles", "roles.authorities"})
    Optional<User> findUserByEmail(String username);

    @Query("Select new tz.go.zanemr.auth.modules.user.UserDto(" +
            "u.id, u.uuid, " +
            "u.firstName," +
            "u.middleName," +
            "u.lastName," +
            "u.facilityId," +
            "u.facilityUuid," +
            "u.facilityName," +
            "u.facilityCode) from User u where u.email=:username")
    Optional<UserDto> findUserDtoByUsername(@Param("username") String username);


    @Query("SELECT u FROM User u WHERE " +
            "LOWER(CONCAT(u.firstName, ' ', u.middleName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<User> searchByFullName(@Param("fullName") String fullName);

}
