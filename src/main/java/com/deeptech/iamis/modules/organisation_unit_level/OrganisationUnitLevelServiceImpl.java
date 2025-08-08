package com.deeptech.iamis.modules.organisation_unit_level;

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
 * Service implementation for managing OrganisationUnitLevels. Provides CRUD operations for OrganisationUnitLevels
 * This service uses {@link OrganisationUnitLevelRepository} for database operations and {@link OrganisationUnitLevelMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for OrganisationUnitLevels.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganisationUnitLevelServiceImpl
  extends SearchService<OrganisationUnitLevel>
  implements OrganisationUnitLevelService {

  private final OrganisationUnitLevelRepository organisationUnitLevelRepository;

  private final OrganisationUnitLevelMapper organisationUnitLevelMapper;

  /**
   * Creates or updates a OrganisationUnitLevel based on the presence of a UUID in the DTO.
   *
   * @param organisationUnitLevelDto the {@link OrganisationUnitLevelDto} containing OrganisationUnitLevel data.
   * @return the saved {@link OrganisationUnitLevelDto}.
   * @throws EntityNotFoundException if the OrganisationUnitLevel is not found for update or there is validation failure.
   */
  @Override
  public OrganisationUnitLevelDto save(
    OrganisationUnitLevelDto organisationUnitLevelDto
  ) {
    // Convert the DTO to an entity object
    OrganisationUnitLevel organisationUnitLevel =
      organisationUnitLevelMapper.toEntity(organisationUnitLevelDto);

    // If the OrganisationUnitLevel has a UUID, fetch the existing OrganisationUnitLevel and perform an update
    if (organisationUnitLevelDto.getUuid() != null) {
      organisationUnitLevel =
        organisationUnitLevelRepository
          .findByUuid(organisationUnitLevelDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "organisationUnitLevel with uuid" +
              organisationUnitLevelDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing OrganisationUnitLevel with new data from the DTO
      organisationUnitLevel =
        organisationUnitLevelMapper.partialUpdate(
          organisationUnitLevelDto,
          organisationUnitLevel
        );
    } else {
      // Set a new UUID for a new OrganisationUnitLevel
      organisationUnitLevel.setUuid(Utils.generateUuid());
    }
    // Save the OrganisationUnitLevel and return the DTO
    organisationUnitLevel =
      organisationUnitLevelRepository.save(organisationUnitLevel);
    return organisationUnitLevelMapper.toDto(organisationUnitLevel);
  }

  /**
   * Finds all OrganisationUnitLevels based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link OrganisationUnitLevelDto}.
   */
  @Override
  public Page<OrganisationUnitLevelDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return organisationUnitLevelRepository
      .findAll(
        createSpecification(OrganisationUnitLevel.class, searchParams),
        pageable
      )
      .map(organisationUnitLevelMapper::toDto);
  }

  /**
   * Finds a OrganisationUnitLevel by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnitLevel to find.
   * @return the {@link OrganisationUnitLevelDto} if found.
   * @throws ValidationException if the OrganisationUnitLevel is not found.
   */
  @Override
  public OrganisationUnitLevelDto findById(UUID uuid) {
    return organisationUnitLevelRepository
      .findByUuid(uuid)
      .map(organisationUnitLevelMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "OrganisationUnitLevel  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a OrganisationUnitLevel by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnitLevel to delete.
   * @throws ValidationException if the OrganisationUnitLevel cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      organisationUnitLevelRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete OrganisationUnitLevel with uuid " + uuid
      );
    }
  }
}
