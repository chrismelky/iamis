package com.deeptech.iamis.modules.organisation_unit;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing OrganisationUnits in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link OrganisationUnit} entities
 * and can define additional functionality to be implemented specific to OrganisationUnit management.
 */
public interface OrganisationUnitService
  extends BaseCrudService<OrganisationUnitDto, OrganisationUnit> {}
