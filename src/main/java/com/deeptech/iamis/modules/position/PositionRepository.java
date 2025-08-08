package com.deeptech.iamis.modules.position;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link Position} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link Position} specific queries and operations.
 */
public interface PositionRepository extends BaseRepository<Position, Long> {}
