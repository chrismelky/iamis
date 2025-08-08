package com.deeptech.iamis.modules.professional_qualification;

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
 * REST controller that provides endpoints for managing ProfessionalQualification within the system.
 * This class handles CRUD operations for ProfessionalQualifications and provides an additional APIs  to ProfessionalQualification.
 * <p>
 * Endpoints:
 * - POST /api/professional-qualifications: Create a new ProfessionalQualification
 * - PUT /api/professional-qualifications/{uuid}: Update an existing ProfessionalQualification
 * - GET /api/professional-qualifications: Fetch all ProfessionalQualifications with optional search and pagination
 * - GET /api/professional-qualifications/{uuid}: Get an existing ProfessionalQualification by its UUID
 * - DELETE /api/professional-qualifications/{uuid}: Delete a ProfessionalQualification by its UUID
 * <p>
 * Uses {@link ProfessionalQualificationService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/professional-qualifications")
public class ProfessionalQualificationResource {

  private final ProfessionalQualificationService professionalQualificationService;

  /**
   * Creates a new ProfessionalQualification in the system.
   *
   * @param dto the {@link ProfessionalQualificationDto} object representing the ProfessionalQualification data.
   * @return {@link CustomApiResponse} containing the created ProfessionalQualification.
   * @throws ValidationException if the provided ProfessionalQualification DTO contains an id or uuid (new ProfessionalQualification should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(
    @Valid @RequestBody ProfessionalQualificationDto dto
  ) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException(
        "New ProfessionalQualification should not have  id or uuid"
      );
    }
    return CustomApiResponse.ok(professionalQualificationService.save(dto));
  }

  /**
   * Updates an existing ProfessionalQualification.
   *
   * @param dto  the {@link ProfessionalQualificationDto} object containing updated ProfessionalQualification data.
   * @param uuid the UUID of the ProfessionalQualification to update.
   * @return {@link CustomApiResponse} containing the updated ProfessionalQualification.
   * @throws ValidationException if the provided ProfessionalQualification DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody ProfessionalQualificationDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated ProfessionalQualification should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(professionalQualificationService.save(dto));
  }

  /**
   * Retrieves a list of ProfessionalQualifications with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering ProfessionalQualifications.
   * @return {@link CustomApiResponse} containing a list of ProfessionalQualifications and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      professionalQualificationService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a ProfessionalQualification by its UUID.
   *
   * @param uuid the UUID of the ProfessionalQualification to retrieve.
   * @return {@link CustomApiResponse} containing the ProfessionalQualification data.
   * @throws ValidationException if no ProfessionalQualification with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(
      professionalQualificationService.findById(uuid)
    );
  }

  /**
   * Deletes a ProfessionalQualification by its UUID.
   *
   * @param uuid the UUID of the ProfessionalQualification to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      professionalQualificationService.delete(uuid);
      return CustomApiResponse.ok(
        "ProfessionalQualification deleted successfully"
      );
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete ProfessionalQualification with uuid " + uuid
      );
    }
  }
}
