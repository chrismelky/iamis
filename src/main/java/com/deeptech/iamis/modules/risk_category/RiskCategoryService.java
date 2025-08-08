package com.deeptech.iamis.modules.risk_category;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing RiskCategories in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link RiskCategory} entities
 * and can define additional functionality to be implemented specific to RiskCategory management.
 */
public interface RiskCategoryService
  extends BaseCrudService<RiskCategoryDto, RiskCategory> {}
