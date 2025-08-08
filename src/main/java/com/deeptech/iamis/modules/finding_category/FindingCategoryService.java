package com.deeptech.iamis.modules.finding_category;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing FindingCategories in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link FindingCategory} entities
 * and can define additional functionality to be implemented specific to FindingCategory management.
 */
public interface FindingCategoryService
  extends BaseCrudService<FindingCategoryDto, FindingCategory> {}
