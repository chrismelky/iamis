package com.deeptech.iamis.modules.risk_rank;

import com.deeptech.iamis.core.SearchService;
import com.deeptech.iamis.core.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing RiskRanks. Provides CRUD operations for RiskRanks
 * This service uses {@link RiskRankRepository} for database operations and {@link RiskRankMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for RiskRanks.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RiskRankServiceImpl
  extends SearchService<RiskRank>
  implements RiskRankService {

  private final RiskRankRepository riskRankRepository;

  private final RiskRankMapper riskRankMapper;

  /**
   * Creates or updates a RiskRank based on the presence of a UUID in the DTO.
   *
   * @param riskRankDto the {@link RiskRankDto} containing RiskRank data.
   * @return the saved {@link RiskRankDto}.
   * @throws EntityNotFoundException if the RiskRank is not found for update or there is validation failure.
   */
  @Override
  public RiskRankDto save(RiskRankDto riskRankDto) {
    // Convert the DTO to an entity object
    RiskRank riskRank = riskRankMapper.toEntity(riskRankDto);

    // If the RiskRank has a UUID, fetch the existing RiskRank and perform an update
    if (riskRankDto.getUuid() != null) {
      riskRank =
        riskRankRepository
          .findByUuid(riskRankDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "riskRank with uuid" + riskRankDto.getUuid() + " not found"
            )
          );
      // Partially update the existing RiskRank with new data from the DTO
      riskRank = riskRankMapper.partialUpdate(riskRankDto, riskRank);
    } else {
      // Set a new UUID for a new RiskRank
      riskRank.setUuid(Utils.generateUuid());
    }
    // Save the RiskRank and return the DTO
    riskRank = riskRankRepository.save(riskRank);
    return riskRankMapper.toDto(riskRank);
  }

  /**
   * Finds all RiskRanks based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link RiskRankDto}.
   */
  @Override
  public Page<RiskRankDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return riskRankRepository
      .findAll(createSpecification(RiskRank.class, searchParams), pageable)
      .map(riskRankMapper::toDto);
  }

  /**
   * Finds a RiskRank by its UUID.
   *
   * @param uuid the UUID of the RiskRank to find.
   * @return the {@link RiskRankDto} if found.
   * @throws ValidationException if the RiskRank is not found.
   */
  @Override
  public RiskRankDto findById(UUID uuid) {
    return riskRankRepository
      .findByUuid(uuid)
      .map(riskRankMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "RiskRank  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a RiskRank by its UUID.
   *
   * @param uuid the UUID of the RiskRank to delete.
   * @throws ValidationException if the RiskRank cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      riskRankRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException("Cannot delete RiskRank with uuid " + uuid);
    }
  }
}
