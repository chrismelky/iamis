package com.deeptech.iamis.modules.organisation_unit_level;

import com.deeptech.iamis.core.AppConstants;
import com.deeptech.iamis.core.CustomApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that provides endpoints for managing OrganisationUnitLevel within the system.
 * This class handles CRUD operations for OrganisationUnitLevels and provides an additional APIs  to OrganisationUnitLevel.
 * <p>
 * Endpoints:
 * - POST /api/organisation-unit-levels: Create a new OrganisationUnitLevel
 * - PUT /api/organisation-unit-levels/{uuid}: Update an existing OrganisationUnitLevel
 * - GET /api/organisation-unit-levels: Fetch all OrganisationUnitLevels with optional search and pagination
 * - GET /api/organisation-unit-levels/{uuid}: Get an existing OrganisationUnitLevel by its UUID
 * - DELETE /api/organisation-unit-levels/{uuid}: Delete a OrganisationUnitLevel by its UUID
 * <p>
 * Uses {@link OrganisationUnitLevelService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/organisation-unit-levels")
public class OrganisationUnitLevelResource {

  private final OrganisationUnitLevelService organisationUnitLevelService;

  /**
   * Creates a new OrganisationUnitLevel in the system.
   *
   * @param dto the {@link OrganisationUnitLevelDto} object representing the OrganisationUnitLevel data.
   * @return {@link CustomApiResponse} containing the created OrganisationUnitLevel.
   * @throws ValidationException if the provided OrganisationUnitLevel DTO contains an id or uuid (new OrganisationUnitLevel should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(
    @Valid @RequestBody OrganisationUnitLevelDto dto
  ) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New OrganisationUnitLevel should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(organisationUnitLevelService.save(dto));
  }

  /**
   * Updates an existing OrganisationUnitLevel.
   *
   * @param dto  the {@link OrganisationUnitLevelDto} object containing updated OrganisationUnitLevel data.
   * @param uuid the UUID of the OrganisationUnitLevel to update.
   * @return {@link CustomApiResponse} containing the updated OrganisationUnitLevel.
   * @throws ValidationException if the provided OrganisationUnitLevel DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody OrganisationUnitLevelDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated OrganisationUnitLevel should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(organisationUnitLevelService.save(dto));
  }

  /**
   * Retrieves a list of OrganisationUnitLevels with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering OrganisationUnitLevels.
   * @return {@link CustomApiResponse} containing a list of OrganisationUnitLevels and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      organisationUnitLevelService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a OrganisationUnitLevel by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnitLevel to retrieve.
   * @return {@link CustomApiResponse} containing the OrganisationUnitLevel data.
   * @throws ValidationException if no OrganisationUnitLevel with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(organisationUnitLevelService.findById(uuid));
  }

  /**
   * Deletes a OrganisationUnitLevel by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnitLevel to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      organisationUnitLevelService.delete(uuid);
      return CustomApiResponse.ok("OrganisationUnitLevel deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete OrganisationUnitLevel with uuid " + uuid
      );
    }
  }
}
