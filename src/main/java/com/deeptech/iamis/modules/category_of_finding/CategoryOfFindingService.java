package com.deeptech.iamis.modules.category_of_finding;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing CategoryOfFindings in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link CategoryOfFinding} entities
 * and can define additional functionality to be implemented specific to CategoryOfFinding management.
 */
public interface CategoryOfFindingService
  extends BaseCrudService<CategoryOfFindingDto, CategoryOfFinding> {}
