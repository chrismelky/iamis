package com.deeptech.iamis.modules.finding_subcategory;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link FindingSubcategory} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link FindingSubcategory} specific queries and operations.
 */
public interface FindingSubcategoryRepository
  extends BaseRepository<FindingSubcategory, Long> {}
