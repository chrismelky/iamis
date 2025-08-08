package com.deeptech.iamis.modules.professional_qualification;

import com.deeptech.iamis.core.BaseRepository;

/**
 * Repository interface for {@link ProfessionalQualification} entities.
 * <p>
 * This interface extends {@link BaseRepository} which provides generic CRUD operations
 * and additional query methods such as findByUuid, deleteByUuid, getReferenceByUuid.
 * It is responsible for interacting with the database for {@link ProfessionalQualification} specific queries and operations.
 */
public interface ProfessionalQualificationRepository
  extends BaseRepository<ProfessionalQualification, Long> {}
