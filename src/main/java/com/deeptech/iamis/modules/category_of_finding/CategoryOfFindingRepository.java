package com.deeptech.iamis.modules.category_of_finding;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link CategoryOfFinding} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link CategoryOfFinding} specific queries and operations.
 */
public interface CategoryOfFindingRepository
  extends BaseRepository<CategoryOfFinding, Long> {}
