package com.deeptech.iamis.modules.department;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing Departments in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link Department} entities
 * and can define additional functionality to be implemented specific to Department management.
 */
public interface DepartmentService
  extends BaseCrudService<DepartmentDto, Department> {}
