package com.deeptech.iamis.modules.risk_category;

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
 * REST controller that provides endpoints for managing RiskCategory within the system.
 * This class handles CRUD operations for RiskCategories and provides an additional APIs  to RiskCategory.
 * <p>
 * Endpoints:
 * - POST /api/risk-categories: Create a new RiskCategory
 * - PUT /api/risk-categories/{uuid}: Update an existing RiskCategory
 * - GET /api/risk-categories: Fetch all RiskCategories with optional search and pagination
 * - GET /api/risk-categories/{uuid}: Get an existing RiskCategory by its UUID
 * - DELETE /api/risk-categories/{uuid}: Delete a RiskCategory by its UUID
 * <p>
 * Uses {@link RiskCategoryService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/risk-categories")
public class RiskCategoryResource {

  private final RiskCategoryService riskCategoryService;

  /**
   * Creates a new RiskCategory in the system.
   *
   * @param dto the {@link RiskCategoryDto} object representing the RiskCategory data.
   * @return {@link CustomApiResponse} containing the created RiskCategory.
   * @throws ValidationException if the provided RiskCategory DTO contains an id or uuid (new RiskCategory should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody RiskCategoryDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New RiskCategory should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(riskCategoryService.save(dto));
  }

  /**
   * Updates an existing RiskCategory.
   *
   * @param dto  the {@link RiskCategoryDto} object containing updated RiskCategory data.
   * @param uuid the UUID of the RiskCategory to update.
   * @return {@link CustomApiResponse} containing the updated RiskCategory.
   * @throws ValidationException if the provided RiskCategory DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody RiskCategoryDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated RiskCategory should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(riskCategoryService.save(dto));
  }

  /**
   * Retrieves a list of RiskCategories with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering RiskCategories.
   * @return {@link CustomApiResponse} containing a list of RiskCategories and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      riskCategoryService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a RiskCategory by its UUID.
   *
   * @param uuid the UUID of the RiskCategory to retrieve.
   * @return {@link CustomApiResponse} containing the RiskCategory data.
   * @throws ValidationException if no RiskCategory with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(riskCategoryService.findById(uuid));
  }

  /**
   * Deletes a RiskCategory by its UUID.
   *
   * @param uuid the UUID of the RiskCategory to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      riskCategoryService.delete(uuid);
      return CustomApiResponse.ok("RiskCategory deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete RiskCategory with uuid " + uuid
      );
    }
  }
}
