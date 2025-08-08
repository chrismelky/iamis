package com.deeptech.iamis.modules.financial_year;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing FinancialYears in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link FinancialYear} entities
 * and can define additional functionality to be implemented specific to FinancialYear management.
 */
public interface FinancialYearService
  extends BaseCrudService<FinancialYearDto, FinancialYear> {}
