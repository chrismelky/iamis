package com.deeptech.iamis.modules.organisation_unit_level;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing OrganisationUnitLevels in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link OrganisationUnitLevel} entities
 * and can define additional functionality to be implemented specific to OrganisationUnitLevel management.
 */
public interface OrganisationUnitLevelService
  extends BaseCrudService<OrganisationUnitLevelDto, OrganisationUnitLevel> {}
