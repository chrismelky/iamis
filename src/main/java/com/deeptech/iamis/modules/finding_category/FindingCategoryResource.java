package com.deeptech.iamis.modules.finding_category;

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
 * REST controller that provides endpoints for managing FindingCategory within the system.
 * This class handles CRUD operations for FindingCategories and provides an additional APIs  to FindingCategory.
 * <p>
 * Endpoints:
 * - POST /api/finding-categories: Create a new FindingCategory
 * - PUT /api/finding-categories/{uuid}: Update an existing FindingCategory
 * - GET /api/finding-categories: Fetch all FindingCategories with optional search and pagination
 * - GET /api/finding-categories/{uuid}: Get an existing FindingCategory by its UUID
 * - DELETE /api/finding-categories/{uuid}: Delete a FindingCategory by its UUID
 * <p>
 * Uses {@link FindingCategoryService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/finding-categories")
public class FindingCategoryResource {

  private final FindingCategoryService findingCategoryService;

  /**
   * Creates a new FindingCategory in the system.
   *
   * @param dto the {@link FindingCategoryDto} object representing the FindingCategory data.
   * @return {@link CustomApiResponse} containing the created FindingCategory.
   * @throws ValidationException if the provided FindingCategory DTO contains an id or uuid (new FindingCategory should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody FindingCategoryDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New FindingCategory should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(findingCategoryService.save(dto));
  }

  /**
   * Updates an existing FindingCategory.
   *
   * @param dto  the {@link FindingCategoryDto} object containing updated FindingCategory data.
   * @param uuid the UUID of the FindingCategory to update.
   * @return {@link CustomApiResponse} containing the updated FindingCategory.
   * @throws ValidationException if the provided FindingCategory DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody FindingCategoryDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated FindingCategory should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(findingCategoryService.save(dto));
  }

  /**
   * Retrieves a list of FindingCategories with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering FindingCategories.
   * @return {@link CustomApiResponse} containing a list of FindingCategories and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      findingCategoryService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a FindingCategory by its UUID.
   *
   * @param uuid the UUID of the FindingCategory to retrieve.
   * @return {@link CustomApiResponse} containing the FindingCategory data.
   * @throws ValidationException if no FindingCategory with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(findingCategoryService.findById(uuid));
  }

  /**
   * Deletes a FindingCategory by its UUID.
   *
   * @param uuid the UUID of the FindingCategory to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      findingCategoryService.delete(uuid);
      return CustomApiResponse.ok("FindingCategory deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FindingCategory with uuid " + uuid
      );
    }
  }
}
