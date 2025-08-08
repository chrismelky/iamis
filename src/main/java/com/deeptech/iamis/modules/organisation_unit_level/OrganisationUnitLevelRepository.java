package com.deeptech.iamis.modules.organisation_unit_level;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link OrganisationUnitLevel} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link OrganisationUnitLevel} specific queries and operations.
 */
public interface OrganisationUnitLevelRepository
  extends BaseRepository<OrganisationUnitLevel, Long> {}
