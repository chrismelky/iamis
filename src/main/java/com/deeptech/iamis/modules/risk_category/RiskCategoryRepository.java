package com.deeptech.iamis.modules.risk_category;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link RiskCategory} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link RiskCategory} specific queries and operations.
 */
public interface RiskCategoryRepository
  extends BaseRepository<RiskCategory, Long> {}
