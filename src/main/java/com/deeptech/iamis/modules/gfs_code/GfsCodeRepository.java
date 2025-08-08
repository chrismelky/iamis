package com.deeptech.iamis.modules.gfs_code;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link GfsCode} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link GfsCode} specific queries and operations.
 */
public interface GfsCodeRepository extends BaseRepository<GfsCode, Long> {}
