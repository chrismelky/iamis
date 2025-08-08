package com.deeptech.iamis.modules.finding_subcategory;

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
 * REST controller that provides endpoints for managing FindingSubcategory within the system.
 * This class handles CRUD operations for FindingSubcategories and provides an additional APIs  to FindingSubcategory.
 * <p>
 * Endpoints:
 * - POST /api/finding-subcategories: Create a new FindingSubcategory
 * - PUT /api/finding-subcategories/{uuid}: Update an existing FindingSubcategory
 * - GET /api/finding-subcategories: Fetch all FindingSubcategories with optional search and pagination
 * - GET /api/finding-subcategories/{uuid}: Get an existing FindingSubcategory by its UUID
 * - DELETE /api/finding-subcategories/{uuid}: Delete a FindingSubcategory by its UUID
 * <p>
 * Uses {@link FindingSubcategoryService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/finding-subcategories")
public class FindingSubcategoryResource {

  private final FindingSubcategoryService findingSubcategoryService;

  /**
   * Creates a new FindingSubcategory in the system.
   *
   * @param dto the {@link FindingSubcategoryDto} object representing the FindingSubcategory data.
   * @return {@link CustomApiResponse} containing the created FindingSubcategory.
   * @throws ValidationException if the provided FindingSubcategory DTO contains an id or uuid (new FindingSubcategory should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(
    @Valid @RequestBody FindingSubcategoryDto dto
  ) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New FindingSubcategory should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(findingSubcategoryService.save(dto));
  }

  /**
   * Updates an existing FindingSubcategory.
   *
   * @param dto  the {@link FindingSubcategoryDto} object containing updated FindingSubcategory data.
   * @param uuid the UUID of the FindingSubcategory to update.
   * @return {@link CustomApiResponse} containing the updated FindingSubcategory.
   * @throws ValidationException if the provided FindingSubcategory DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody FindingSubcategoryDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated FindingSubcategory should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(findingSubcategoryService.save(dto));
  }

  /**
   * Retrieves a list of FindingSubcategories with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering FindingSubcategories.
   * @return {@link CustomApiResponse} containing a list of FindingSubcategories and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Long findingCategoryId,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      findingSubcategoryService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a FindingSubcategory by its UUID.
   *
   * @param uuid the UUID of the FindingSubcategory to retrieve.
   * @return {@link CustomApiResponse} containing the FindingSubcategory data.
   * @throws ValidationException if no FindingSubcategory with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(findingSubcategoryService.findById(uuid));
  }

  /**
   * Deletes a FindingSubcategory by its UUID.
   *
   * @param uuid the UUID of the FindingSubcategory to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      findingSubcategoryService.delete(uuid);
      return CustomApiResponse.ok("FindingSubcategory deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FindingSubcategory with uuid " + uuid
      );
    }
  }
}
