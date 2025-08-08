package com.deeptech.iamis.modules.internal_control_type;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing InternalControlTypes in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link InternalControlType} entities
 * and can define additional functionality to be implemented specific to InternalControlType management.
 */
public interface InternalControlTypeService
  extends BaseCrudService<InternalControlTypeDto, InternalControlType> {}
