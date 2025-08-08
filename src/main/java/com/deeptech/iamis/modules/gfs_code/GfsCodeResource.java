package com.deeptech.iamis.modules.gfs_code;

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
 * REST controller that provides endpoints for managing GfsCode within the system.
 * This class handles CRUD operations for GfsCodes and provides an additional APIs  to GfsCode.
 * <p>
 * Endpoints:
 * - POST /api/gfs-codes: Create a new GfsCode
 * - PUT /api/gfs-codes/{uuid}: Update an existing GfsCode
 * - GET /api/gfs-codes: Fetch all GfsCodes with optional search and pagination
 * - GET /api/gfs-codes/{uuid}: Get an existing GfsCode by its UUID
 * - DELETE /api/gfs-codes/{uuid}: Delete a GfsCode by its UUID
 * <p>
 * Uses {@link GfsCodeService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/gfs-codes")
public class GfsCodeResource {

  private final GfsCodeService gfsCodeService;

  /**
   * Creates a new GfsCode in the system.
   *
   * @param dto the {@link GfsCodeDto} object representing the GfsCode data.
   * @return {@link CustomApiResponse} containing the created GfsCode.
   * @throws ValidationException if the provided GfsCode DTO contains an id or uuid (new GfsCode should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody GfsCodeDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException("New GfsCode should not have  id or uuid");
    }
    return CustomApiResponse.ok(gfsCodeService.save(dto));
  }

  /**
   * Updates an existing GfsCode.
   *
   * @param dto  the {@link GfsCodeDto} object containing updated GfsCode data.
   * @param uuid the UUID of the GfsCode to update.
   * @return {@link CustomApiResponse} containing the updated GfsCode.
   * @throws ValidationException if the provided GfsCode DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody GfsCodeDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated GfsCode should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(gfsCodeService.save(dto));
  }

  /**
   * Retrieves a list of GfsCodes with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering GfsCodes.
   * @return {@link CustomApiResponse} containing a list of GfsCodes and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(gfsCodeService.findAll(pageable, searchParams));
  }

  /**
   * Retrieves a GfsCode by its UUID.
   *
   * @param uuid the UUID of the GfsCode to retrieve.
   * @return {@link CustomApiResponse} containing the GfsCode data.
   * @throws ValidationException if no GfsCode with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(gfsCodeService.findById(uuid));
  }

  /**
   * Deletes a GfsCode by its UUID.
   *
   * @param uuid the UUID of the GfsCode to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      gfsCodeService.delete(uuid);
      return CustomApiResponse.ok("GfsCode deleted successfully");
    } catch (Exception e) {
      throw new ValidationException("Cannot delete GfsCode with uuid " + uuid);
    }
  }
}
