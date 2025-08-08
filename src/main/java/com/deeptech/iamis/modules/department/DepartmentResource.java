package com.deeptech.iamis.modules.department;

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
 * REST controller that provides endpoints for managing Department within the system.
 * This class handles CRUD operations for Departments and provides an additional APIs  to Department.
 * <p>
 * Endpoints:
 * - POST /api/departments: Create a new Department
 * - PUT /api/departments/{uuid}: Update an existing Department
 * - GET /api/departments: Fetch all Departments with optional search and pagination
 * - GET /api/departments/{uuid}: Get an existing Department by its UUID
 * - DELETE /api/departments/{uuid}: Delete a Department by its UUID
 * <p>
 * Uses {@link DepartmentService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/departments")
public class DepartmentResource {

  private final DepartmentService departmentService;

  /**
   * Creates a new Department in the system.
   *
   * @param dto the {@link DepartmentDto} object representing the Department data.
   * @return {@link CustomApiResponse} containing the created Department.
   * @throws ValidationException if the provided Department DTO contains an id or uuid (new Department should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody DepartmentDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New Department should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(departmentService.save(dto));
  }

  /**
   * Updates an existing Department.
   *
   * @param dto  the {@link DepartmentDto} object containing updated Department data.
   * @param uuid the UUID of the Department to update.
   * @return {@link CustomApiResponse} containing the updated Department.
   * @throws ValidationException if the provided Department DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody DepartmentDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated Department should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(departmentService.save(dto));
  }

  /**
   * Retrieves a list of Departments with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering Departments.
   * @return {@link CustomApiResponse} containing a list of Departments and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Long organisationUnitId,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      departmentService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a Department by its UUID.
   *
   * @param uuid the UUID of the Department to retrieve.
   * @return {@link CustomApiResponse} containing the Department data.
   * @throws ValidationException if no Department with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(departmentService.findById(uuid));
  }

  /**
   * Deletes a Department by its UUID.
   *
   * @param uuid the UUID of the Department to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      departmentService.delete(uuid);
      return CustomApiResponse.ok("Department deleted successfully");
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete Department with uuid " + uuid
      );
    }
  }
}
