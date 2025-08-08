package com.deeptech.iamis.modules.organisation_unit;

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
 * REST controller that provides endpoints for managing OrganisationUnit within the system.
 * This class handles CRUD operations for OrganisationUnits and provides an additional APIs  to OrganisationUnit.
 * <p>
 * Endpoints:
 * - POST /api/organisation-units: Create a new OrganisationUnit
 * - PUT /api/organisation-units/{uuid}: Update an existing OrganisationUnit
 * - GET /api/organisation-units: Fetch all OrganisationUnits with optional search and pagination
 * - GET /api/organisation-units/{uuid}: Get an existing OrganisationUnit by its UUID
 * - DELETE /api/organisation-units/{uuid}: Delete a OrganisationUnit by its UUID
 * <p>
 * Uses {@link OrganisationUnitService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/organisation-units")
public class OrganisationUnitResource {

  private final OrganisationUnitService organisationUnitService;

  /**
   * Creates a new OrganisationUnit in the system.
   *
   * @param dto the {@link OrganisationUnitDto} object representing the OrganisationUnit data.
   * @return {@link CustomApiResponse} containing the created OrganisationUnit.
   * @throws ValidationException if the provided OrganisationUnit DTO contains an id or uuid (new OrganisationUnit should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody OrganisationUnitDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New OrganisationUnit should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(organisationUnitService.save(dto));
  }

  /**
   * Updates an existing OrganisationUnit.
   *
   * @param dto  the {@link OrganisationUnitDto} object containing updated OrganisationUnit data.
   * @param uuid the UUID of the OrganisationUnit to update.
   * @return {@link CustomApiResponse} containing the updated OrganisationUnit.
   * @throws ValidationException if the provided OrganisationUnit DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody OrganisationUnitDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated OrganisationUnit should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(organisationUnitService.save(dto));
  }

  /**
   * Retrieves a list of OrganisationUnits with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering OrganisationUnits.
   * @return {@link CustomApiResponse} containing a list of OrganisationUnits and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Long parentId,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      organisationUnitService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a OrganisationUnit by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnit to retrieve.
   * @return {@link CustomApiResponse} containing the OrganisationUnit data.
   * @throws ValidationException if no OrganisationUnit with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(organisationUnitService.findById(uuid));
  }

  /**
   * Deletes a OrganisationUnit by its UUID.
   *
   * @param uuid the UUID of the OrganisationUnit to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      organisationUnitService.delete(uuid);
      return CustomApiResponse.ok("OrganisationUnit deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete OrganisationUnit with uuid " + uuid
      );
    }
  }
}
