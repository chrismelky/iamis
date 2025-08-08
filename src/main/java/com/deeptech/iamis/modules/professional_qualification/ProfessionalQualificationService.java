package com.deeptech.iamis.modules.professional_qualification;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing ProfessionalQualifications in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link ProfessionalQualification} entities
 * and can define additional functionality to be implemented specific to ProfessionalQualification management.
 */
public interface ProfessionalQualificationService
  extends
    BaseCrudService<ProfessionalQualificationDto, ProfessionalQualification> {}
