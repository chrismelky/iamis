package com.deeptech.iamis.modules.department;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link Department} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link Department} specific queries and operations.
 */
public interface DepartmentRepository
  extends BaseRepository<Department, Long> {}
