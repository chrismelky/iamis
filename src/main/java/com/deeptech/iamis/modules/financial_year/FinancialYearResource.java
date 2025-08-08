package com.deeptech.iamis.modules.financial_year;

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
 * REST controller that provides endpoints for managing FinancialYear within the system.
 * This class handles CRUD operations for FinancialYears and provides an additional APIs  to FinancialYear.
 * <p>
 * Endpoints:
 * - POST /api/financial-years: Create a new FinancialYear
 * - PUT /api/financial-years/{uuid}: Update an existing FinancialYear
 * - GET /api/financial-years: Fetch all FinancialYears with optional search and pagination
 * - GET /api/financial-years/{uuid}: Get an existing FinancialYear by its UUID
 * - DELETE /api/financial-years/{uuid}: Delete a FinancialYear by its UUID
 * <p>
 * Uses {@link FinancialYearService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/financial-years")
public class FinancialYearResource {

  private final FinancialYearService financialYearService;

  /**
   * Creates a new FinancialYear in the system.
   *
   * @param dto the {@link FinancialYearDto} object representing the FinancialYear data.
   * @return {@link CustomApiResponse} containing the created FinancialYear.
   * @throws ValidationException if the provided FinancialYear DTO contains an id or uuid (new FinancialYear should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody FinancialYearDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New FinancialYear should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(financialYearService.save(dto));
  }

  /**
   * Updates an existing FinancialYear.
   *
   * @param dto  the {@link FinancialYearDto} object containing updated FinancialYear data.
   * @param uuid the UUID of the FinancialYear to update.
   * @return {@link CustomApiResponse} containing the updated FinancialYear.
   * @throws ValidationException if the provided FinancialYear DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody FinancialYearDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated FinancialYear should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(financialYearService.save(dto));
  }

  /**
   * Retrieves a list of FinancialYears with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering FinancialYears.
   * @return {@link CustomApiResponse} containing a list of FinancialYears and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      financialYearService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a FinancialYear by its UUID.
   *
   * @param uuid the UUID of the FinancialYear to retrieve.
   * @return {@link CustomApiResponse} containing the FinancialYear data.
   * @throws ValidationException if no FinancialYear with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(financialYearService.findById(uuid));
  }

  /**
   * Deletes a FinancialYear by its UUID.
   *
   * @param uuid the UUID of the FinancialYear to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      financialYearService.delete(uuid);
      return CustomApiResponse.ok("FinancialYear deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FinancialYear with uuid " + uuid
      );
    }
  }
}
