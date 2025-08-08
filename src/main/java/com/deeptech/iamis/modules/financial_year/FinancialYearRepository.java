package com.deeptech.iamis.modules.financial_year;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link FinancialYear} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link FinancialYear} specific queries and operations.
 */
public interface FinancialYearRepository
  extends BaseRepository<FinancialYear, Long> {}
