package com.deeptech.iamis.modules.position;

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
 * REST controller that provides endpoints for managing Position within the system.
 * This class handles CRUD operations for Positions and provides an additional APIs  to Position.
 * <p>
 * Endpoints:
 * - POST /api/positions: Create a new Position
 * - PUT /api/positions/{uuid}: Update an existing Position
 * - GET /api/positions: Fetch all Positions with optional search and pagination
 * - GET /api/positions/{uuid}: Get an existing Position by its UUID
 * - DELETE /api/positions/{uuid}: Delete a Position by its UUID
 * <p>
 * Uses {@link PositionService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/positions")
public class PositionResource {

  private final PositionService positionService;

  /**
   * Creates a new Position in the system.
   *
   * @param dto the {@link PositionDto} object representing the Position data.
   * @return {@link CustomApiResponse} containing the created Position.
   * @throws ValidationException if the provided Position DTO contains an id or uuid (new Position should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody PositionDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException("New Position should not have  id or uuid");
    }
    return CustomApiResponse.ok(positionService.save(dto));
  }

  /**
   * Updates an existing Position.
   *
   * @param dto  the {@link PositionDto} object containing updated Position data.
   * @param uuid the UUID of the Position to update.
   * @return {@link CustomApiResponse} containing the updated Position.
   * @throws ValidationException if the provided Position DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody PositionDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated Position should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(positionService.save(dto));
  }

  /**
   * Retrieves a list of Positions with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering Positions.
   * @return {@link CustomApiResponse} containing a list of Positions and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      positionService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a Position by its UUID.
   *
   * @param uuid the UUID of the Position to retrieve.
   * @return {@link CustomApiResponse} containing the Position data.
   * @throws ValidationException if no Position with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(positionService.findById(uuid));
  }

  /**
   * Deletes a Position by its UUID.
   *
   * @param uuid the UUID of the Position to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      positionService.delete(uuid);
      return CustomApiResponse.ok("Position deleted successfully");
    } catch (Exception e) {
      throw new ValidationException("Cannot delete Position with uuid " + uuid);
    }
  }
}
