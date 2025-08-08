package com.deeptech.iamis.modules.category_of_finding;

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
 * REST controller that provides endpoints for managing CategoryOfFinding within the system.
 * This class handles CRUD operations for CategoryOfFindings and provides an additional APIs  to CategoryOfFinding.
 * <p>
 * Endpoints:
 * - POST /api/category-of-findings: Create a new CategoryOfFinding
 * - PUT /api/category-of-findings/{uuid}: Update an existing CategoryOfFinding
 * - GET /api/category-of-findings: Fetch all CategoryOfFindings with optional search and pagination
 * - GET /api/category-of-findings/{uuid}: Get an existing CategoryOfFinding by its UUID
 * - DELETE /api/category-of-findings/{uuid}: Delete a CategoryOfFinding by its UUID
 * <p>
 * Uses {@link CategoryOfFindingService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/category-of-findings")
public class CategoryOfFindingResource {

  private final CategoryOfFindingService categoryOfFindingService;

  /**
   * Creates a new CategoryOfFinding in the system.
   *
   * @param dto the {@link CategoryOfFindingDto} object representing the CategoryOfFinding data.
   * @return {@link CustomApiResponse} containing the created CategoryOfFinding.
   * @throws ValidationException if the provided CategoryOfFinding DTO contains an id or uuid (new CategoryOfFinding should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(
    @Valid @RequestBody CategoryOfFindingDto dto
  ) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New CategoryOfFinding should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(categoryOfFindingService.save(dto));
  }

  /**
   * Updates an existing CategoryOfFinding.
   *
   * @param dto  the {@link CategoryOfFindingDto} object containing updated CategoryOfFinding data.
   * @param uuid the UUID of the CategoryOfFinding to update.
   * @return {@link CustomApiResponse} containing the updated CategoryOfFinding.
   * @throws ValidationException if the provided CategoryOfFinding DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody CategoryOfFindingDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated CategoryOfFinding should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(categoryOfFindingService.save(dto));
  }

  /**
   * Retrieves a list of CategoryOfFindings with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering CategoryOfFindings.
   * @return {@link CustomApiResponse} containing a list of CategoryOfFindings and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      categoryOfFindingService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a CategoryOfFinding by its UUID.
   *
   * @param uuid the UUID of the CategoryOfFinding to retrieve.
   * @return {@link CustomApiResponse} containing the CategoryOfFinding data.
   * @throws ValidationException if no CategoryOfFinding with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(categoryOfFindingService.findById(uuid));
  }

  /**
   * Deletes a CategoryOfFinding by its UUID.
   *
   * @param uuid the UUID of the CategoryOfFinding to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      categoryOfFindingService.delete(uuid);
      return CustomApiResponse.ok("CategoryOfFinding deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete CategoryOfFinding with uuid " + uuid
      );
    }
  }
}
