package com.deeptech.iamis.modules.organisation_unit;

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
 * Service implementation for managing OrganisationUnits. Provides CRUD operations for OrganisationUnits
 * This service uses {@link OrganisationUnitRepository} for database operations and {@link OrganisationUnitMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for OrganisationUnits.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganisationUnitServiceImpl
  extends SearchService<OrganisationUnit>
  implements OrganisationUnitService {

  private final OrganisationUnitRepository organisationUnitRepository;

  private final OrganisationUnitMapper organisationUnitMapper;

  /**
   * Creates or updates a OrganisationUnit based on the presence of a UUID in the DTO.
   *
   * @param organisationUnitDto the {@link OrganisationUnitDto} containing OrganisationUnit data.
   * @return the saved {@link OrganisationUnitDto}.
   * @throws EntityNotFoundException if the OrganisationUnit is not found for update or there is validation failure.
   */
  @Override
  public OrganisationUnitDto save(OrganisationUnitDto organisationUnitDto) {
    // Convert the DTO to an entity object
    OrganisationUnit organisationUnit = organisationUnitMapper.toEntity(
      organisationUnitDto
    );

    // If the OrganisationUnit has a UUID, fetch the existing OrganisationUnit and perform an update
    if (organisationUnitDto.getUuid() != null) {
      organisationUnit =
        organisationUnitRepository
          .findByUuid(organisationUnitDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "organisationUnit with uuid" +
              organisationUnitDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing OrganisationUnit with new data from the DTO
      organisationUnit =
        organisationUnitMapper.partialUpdate(
          organisationUnitDto,
          organisationUnit
        );
    } else {
      // Set a new UUID for a new OrganisationUnit
      organisationUnit.setUuid(Utils.generateUuid());
    }
    // Save the OrganisationUnit and return the DTO
    organisationUnit = organisationUnitRepository.save(organisationUnit);
    return organisationUnitMapper.toDto(organisationUnit);
  }

  /**
   * Finds all OrganisationUnits based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link OrganisationUnitDto}.
   */
  @Override
  public Page<OrganisationUnitDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return organisationUnitRepository
      .findAll(
        createSpecification(OrganisationUnit.class, searchParams),
        pageable
      )
      .map(organisationUnitMapper::toDto);
  }

  /**
   * Finds a OrganisationUnit by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnit to find.
   * @return the {@link OrganisationUnitDto} if found.
   * @throws ValidationException if the OrganisationUnit is not found.
   */
  @Override
  public OrganisationUnitDto findById(UUID uuid) {
    return organisationUnitRepository
      .findByUuid(uuid)
      .map(organisationUnitMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "OrganisationUnit  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a OrganisationUnit by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnit to delete.
   * @throws ValidationException if the OrganisationUnit cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      organisationUnitRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete OrganisationUnit with uuid " + uuid
      );
    }
  }
}
