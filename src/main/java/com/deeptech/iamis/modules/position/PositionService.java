package com.deeptech.iamis.modules.position;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing Positions in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link Position} entities
 * and can define additional functionality to be implemented specific to Position management.
 */
public interface PositionService
  extends BaseCrudService<PositionDto, Position> {}
