package com.deeptech.iamis.modules.period;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing Periods in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link Period} entities
 * and can define additional functionality to be implemented specific to Period management.
 */
public interface PeriodService extends BaseCrudService<PeriodDto, Period> {}
