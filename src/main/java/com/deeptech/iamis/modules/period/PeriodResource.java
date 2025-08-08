package com.deeptech.iamis.modules.period;

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
 * REST controller that provides endpoints for managing Period within the system.
 * This class handles CRUD operations for Periods and provides an additional APIs  to Period.
 * <p>
 * Endpoints:
 * - POST /api/periods: Create a new Period
 * - PUT /api/periods/{uuid}: Update an existing Period
 * - GET /api/periods: Fetch all Periods with optional search and pagination
 * - GET /api/periods/{uuid}: Get an existing Period by its UUID
 * - DELETE /api/periods/{uuid}: Delete a Period by its UUID
 * <p>
 * Uses {@link PeriodService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/periods")
public class PeriodResource {

  private final PeriodService periodService;

  /**
   * Creates a new Period in the system.
   *
   * @param dto the {@link PeriodDto} object representing the Period data.
   * @return {@link CustomApiResponse} containing the created Period.
   * @throws ValidationException if the provided Period DTO contains an id or uuid (new Period should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody PeriodDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException("New Period should not have  id or uuid");
    }
    return CustomApiResponse.ok(periodService.save(dto));
  }

  /**
   * Updates an existing Period.
   *
   * @param dto  the {@link PeriodDto} object containing updated Period data.
   * @param uuid the UUID of the Period to update.
   * @return {@link CustomApiResponse} containing the updated Period.
   * @throws ValidationException if the provided Period DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody PeriodDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated Period should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(periodService.save(dto));
  }

  /**
   * Retrieves a list of Periods with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering Periods.
   * @return {@link CustomApiResponse} containing a list of Periods and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Long financialYearId,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(periodService.findAll(pageable, searchParams));
  }

  /**
   * Retrieves a Period by its UUID.
   *
   * @param uuid the UUID of the Period to retrieve.
   * @return {@link CustomApiResponse} containing the Period data.
   * @throws ValidationException if no Period with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(periodService.findById(uuid));
  }

  /**
   * Deletes a Period by its UUID.
   *
   * @param uuid the UUID of the Period to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      periodService.delete(uuid);
      return CustomApiResponse.ok("Period deleted successfully");
    } catch (Exception e) {
      throw new ValidationException("Cannot delete Period with uuid " + uuid);
    }
  }
}
