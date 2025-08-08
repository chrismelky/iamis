package com.deeptech.iamis.modules.risk_rank;

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
 * REST controller that provides endpoints for managing RiskRank within the system.
 * This class handles CRUD operations for RiskRanks and provides an additional APIs  to RiskRank.
 * <p>
 * Endpoints:
 * - POST /api/risk-ranks: Create a new RiskRank
 * - PUT /api/risk-ranks/{uuid}: Update an existing RiskRank
 * - GET /api/risk-ranks: Fetch all RiskRanks with optional search and pagination
 * - GET /api/risk-ranks/{uuid}: Get an existing RiskRank by its UUID
 * - DELETE /api/risk-ranks/{uuid}: Delete a RiskRank by its UUID
 * <p>
 * Uses {@link RiskRankService} to perform business logic.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_PREFIX + "/risk-ranks")
public class RiskRankResource {

  private final RiskRankService riskRankService;

  /**
   * Creates a new RiskRank in the system.
   *
   * @param dto the {@link RiskRankDto} object representing the RiskRank data.
   * @return {@link CustomApiResponse} containing the created RiskRank.
   * @throws ValidationException if the provided RiskRank DTO contains an id or uuid (new RiskRank should not have an id or uuid).
   */
  @PostMapping
  public CustomApiResponse create(@Valid @RequestBody RiskRankDto dto) {
    if (dto.getUuid() != null || dto.getId() != null) {
      throw new ValidationException("New RiskRank should not have  id or uuid");
    }
    return CustomApiResponse.ok(riskRankService.save(dto));
  }

  /**
   * Updates an existing RiskRank.
   *
   * @param dto  the {@link RiskRankDto} object containing updated RiskRank data.
   * @param uuid the UUID of the RiskRank to update.
   * @return {@link CustomApiResponse} containing the updated RiskRank.
   * @throws ValidationException if the provided RiskRank DTO does not have an uuid or if the UUIDs do not match.
   */
  @PutMapping("/{uuid}")
  public CustomApiResponse update(
    @Valid @RequestBody RiskRankDto dto,
    @PathVariable UUID uuid
  ) {
    if (dto.getUuid() == null || dto.getId() == null) {
      throw new ValidationException(
        "Updated RiskRank should  have an id and uuid"
      );
    }
    if (!uuid.equals(dto.getUuid())) {
      throw new ValidationException("Uuid mismatch");
    }
    return CustomApiResponse.ok(riskRankService.save(dto));
  }

  /**
   * Retrieves a list of RiskRanks with pagination and optional search parameters.
   *
   * @param pageable the {@link Pageable} object to control pagination.
   * @param searchParams a map of search parameters (fields names) for filtering RiskRanks.
   * @return {@link CustomApiResponse} containing a list of RiskRanks and pagination information.
   */
  @GetMapping
  public CustomApiResponse get(
    Pageable pageable,
    @RequestParam Map<String, Object> searchParams
  ) {
    return CustomApiResponse.ok(
      riskRankService.findAll(pageable, searchParams)
    );
  }

  /**
   * Retrieves a RiskRank by its UUID.
   *
   * @param uuid the UUID of the RiskRank to retrieve.
   * @return {@link CustomApiResponse} containing the RiskRank data.
   * @throws ValidationException if no RiskRank with the given UUID is found.
   */
  @GetMapping("/{uuid}")
  public CustomApiResponse findById(@PathVariable UUID uuid) {
    return CustomApiResponse.ok(riskRankService.findById(uuid));
  }

  /**
   * Deletes a RiskRank by its UUID.
   *
   * @param uuid the UUID of the RiskRank to delete.
   * @return {@link CustomApiResponse} confirming the successful deletion.
   */
  @DeleteMapping("/{uuid}")
  public CustomApiResponse delete(@PathVariable UUID uuid) {
    try {
      riskRankService.delete(uuid);
      return CustomApiResponse.ok("RiskRank deleted successfully");
    } catch (Exception e) {
      throw new ValidationException("Cannot delete RiskRank with uuid " + uuid);
    }
  }
}
