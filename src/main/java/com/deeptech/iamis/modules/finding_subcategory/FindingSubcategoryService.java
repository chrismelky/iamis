package com.deeptech.iamis.modules.finding_subcategory;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing FindingSubcategories in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link FindingSubcategory} entities
 * and can define additional functionality to be implemented specific to FindingSubcategory management.
 */
public interface FindingSubcategoryService
  extends BaseCrudService<FindingSubcategoryDto, FindingSubcategory> {}
