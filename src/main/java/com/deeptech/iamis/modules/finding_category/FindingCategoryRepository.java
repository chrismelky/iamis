package com.deeptech.iamis.modules.finding_category;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link FindingCategory} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link FindingCategory} specific queries and operations.
 */
public interface FindingCategoryRepository
  extends BaseRepository<FindingCategory, Long> {}
