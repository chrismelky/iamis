package com.deeptech.iamis.modules.organisation_unit;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link OrganisationUnit} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link OrganisationUnit} specific queries and operations.
 */
public interface OrganisationUnitRepository
  extends BaseRepository<OrganisationUnit, Long> {}
