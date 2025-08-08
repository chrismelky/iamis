package com.deeptech.iamis.modules.professional_qualification;

import com.deeptech.iamis.core.SearchService;
import com.deeptech.iamis.core.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing ProfessionalQualifications. Provides CRUD operations for ProfessionalQualifications
 * This service uses {@link ProfessionalQualificationRepository} for database operations and {@link ProfessionalQualificationMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for ProfessionalQualifications.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfessionalQualificationServiceImpl
  extends SearchService<ProfessionalQualification>
  implements ProfessionalQualificationService {

  private final ProfessionalQualificationRepository professionalQualificationRepository;

  private final ProfessionalQualificationMapper professionalQualificationMapper;

  /**
   * Creates or updates a ProfessionalQualification based on the presence of a UUID in the DTO.
   *
   * @param professionalQualificationDto the {@link ProfessionalQualificationDto} containing ProfessionalQualification data.
   * @return the saved {@link ProfessionalQualificationDto}.
   * @throws EntityNotFoundException if the ProfessionalQualification is not found for update or there is validation failure.
   */
  @Override
  public ProfessionalQualificationDto save(
    ProfessionalQualificationDto professionalQualificationDto
  ) {
    // Convert the DTO to an entity object
    ProfessionalQualification professionalQualification =
      professionalQualificationMapper.toEntity(professionalQualificationDto);

    // If the ProfessionalQualification has a UUID, fetch the existing ProfessionalQualification and perform an update
    if (professionalQualificationDto.getUuid() != null) {
      professionalQualification =
        professionalQualificationRepository
          .findByUuid(professionalQualificationDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "professionalQualification with uuid" +
              professionalQualificationDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing ProfessionalQualification with new data from the DTO
      professionalQualification =
        professionalQualificationMapper.partialUpdate(
          professionalQualificationDto,
          professionalQualification
        );
    } else {
      // Set a new UUID for a new ProfessionalQualification
      professionalQualification.setUuid(Utils.generateUuid());
    }
    // Save the ProfessionalQualification and return the DTO
    professionalQualification =
      professionalQualificationRepository.save(professionalQualification);
    return professionalQualificationMapper.toDto(professionalQualification);
  }

  /**
   * Finds all ProfessionalQualifications based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link ProfessionalQualificationDto}.
   */
  @Override
  public Page<ProfessionalQualificationDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return professionalQualificationRepository
      .findAll(
        createSpecification(ProfessionalQualification.class, searchParams),
        pageable
      )
      .map(professionalQualificationMapper::toDto);
  }

  /**
   * Finds a ProfessionalQualification by its UUID.
   *
   * @param uuid the UUID of the ProfessionalQualification to find.
   * @return the {@link ProfessionalQualificationDto} if found.
   * @throws ValidationException if the ProfessionalQualification is not found.
   */
  @Override
  public ProfessionalQualificationDto findById(UUID uuid) {
    return professionalQualificationRepository
      .findByUuid(uuid)
      .map(professionalQualificationMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "ProfessionalQualification  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a ProfessionalQualification by its UUID.
   *
   * @param uuid the UUID of the ProfessionalQualification to delete.
   * @throws ValidationException if the ProfessionalQualification cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      professionalQualificationRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete ProfessionalQualification with uuid " + uuid
      );
    }
  }
}
