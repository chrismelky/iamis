package tz.go.zanemr.auth.modules.role;

import tz.go.zanemr.auth.core.BaseRepository;

/**
 * Repository interface for {@link Role} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link Role} specific queries and operations.
 */
public interface RoleRepository extends BaseRepository<Role, Long> {

}