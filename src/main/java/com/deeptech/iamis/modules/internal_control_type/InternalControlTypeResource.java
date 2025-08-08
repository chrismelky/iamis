package com.deeptech.iamis.modules.internal_control_type;

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
 * REST controller that provides endpoints for managing InternalControlType within the system.
 * This class handles CRUD operations for InternalControlTypes and provides an additional APIs  to InternalControlType.
 * <p>
 * Endpoints:
 * - POST /api/internal-control-types: Create a new InternalControlType
 * - PUT /api/internal-control-types/{uuid}: Update an existing InternalControlType
 * - GET /api/internal-control-types: Fetch all InternalControlTypes with optional search and pagination
 * - GET /api/internal-control-types/{uuid}: Get an existing InternalControlType by its UUID
 * - DELETE /api/internal-control-types/{uuid}: Delete a InternalControlType by its UUID
 * <p>
 * Uses {@link InternalControlTypeService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/internal-control-types")
public class InternalControlTypeResource {

  private final InternalControlTypeService internalControlTypeService;

  /**
   * Creates a new InternalControlType in the system.
   *
   * @param dto the {@link InternalControlTypeDto} object representing the InternalControlType data.
   * @return {@link CustomApiResponse} containing the created InternalControlType.
   * @throws ValidationException if the provided InternalControlType DTO contains an id or uuid (new InternalControlType should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(
    @Valid @RequestBody InternalControlTypeDto dto
  ) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New InternalControlType should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(internalControlTypeService.save(dto));
  }

  /**
   * Updates an existing InternalControlType.
   *
   * @param dto  the {@link InternalControlTypeDto} object containing updated InternalControlType data.
   * @param uuid the UUID of the InternalControlType to update.
   * @return {@link CustomApiResponse} containing the updated InternalControlType.
   * @throws ValidationException if the provided InternalControlType DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody InternalControlTypeDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated InternalControlType should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(internalControlTypeService.save(dto));
  }

  /**
   * Retrieves a list of InternalControlTypes with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering InternalControlTypes.
   * @return {@link CustomApiResponse} containing a list of InternalControlTypes and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      internalControlTypeService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a InternalControlType by its UUID.
   *
   * @param uuid the UUID of the InternalControlType to retrieve.
   * @return {@link CustomApiResponse} containing the InternalControlType data.
   * @throws ValidationException if no InternalControlType with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(internalControlTypeService.findById(uuid));
  }

  /**
   * Deletes a InternalControlType by its UUID.
   *
   * @param uuid the UUID of the InternalControlType to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      internalControlTypeService.delete(uuid);
      return CustomApiResponse.ok("InternalControlType deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete InternalControlType with uuid " + uuid
      );
    }
  }
}
