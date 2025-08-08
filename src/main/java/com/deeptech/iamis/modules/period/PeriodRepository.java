package com.deeptech.iamis.modules.period;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link Period} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link Period} specific queries and operations.
 */
public interface PeriodRepository extends BaseRepository<Period, Long> {}
