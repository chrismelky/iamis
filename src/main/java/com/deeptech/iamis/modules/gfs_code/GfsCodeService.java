package com.deeptech.iamis.modules.gfs_code;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing GfsCodes in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link GfsCode} entities
 * and can define additional functionality to be implemented specific to GfsCode management.
 */
public interface GfsCodeService extends BaseCrudService<GfsCodeDto, GfsCode> {}
