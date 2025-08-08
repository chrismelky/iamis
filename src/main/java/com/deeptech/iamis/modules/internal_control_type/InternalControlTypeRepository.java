package com.deeptech.iamis.modules.internal_control_type;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link InternalControlType} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link InternalControlType} specific queries and operations.
 */
public interface InternalControlTypeRepository
  extends BaseRepository<InternalControlType, Long> {}
