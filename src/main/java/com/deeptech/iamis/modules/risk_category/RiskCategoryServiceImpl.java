package com.deeptech.iamis.modules.risk_category;

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
 * Service implementation for managing RiskCategories. Provides CRUD operations for RiskCategories
 * This service uses {@link RiskCategoryRepository} for database operations and {@link RiskCategoryMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for RiskCategories.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RiskCategoryServiceImpl
  extends SearchService<RiskCategory>
  implements RiskCategoryService {

  private final RiskCategoryRepository riskCategoryRepository;

  private final RiskCategoryMapper riskCategoryMapper;

  /**
   * Creates or updates a RiskCategory based on the presence of a UUID in the DTO.
   *
   * @param riskCategoryDto the {@link RiskCategoryDto} containing RiskCategory data.
   * @return the saved {@link RiskCategoryDto}.
   * @throws EntityNotFoundException if the RiskCategory is not found for update or there is validation failure.
   */
  @Override
  public RiskCategoryDto save(RiskCategoryDto riskCategoryDto) {
    // Convert the DTO to an entity object
    RiskCategory riskCategory = riskCategoryMapper.toEntity(riskCategoryDto);

    // If the RiskCategory has a UUID, fetch the existing RiskCategory and perform an update
    if (riskCategoryDto.getUuid() != null) {
      riskCategory =
        riskCategoryRepository
          .findByUuid(riskCategoryDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "riskCategory with uuid" +
              riskCategoryDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing RiskCategory with new data from the DTO
      riskCategory =
        riskCategoryMapper.partialUpdate(riskCategoryDto, riskCategory);
    } else {
      // Set a new UUID for a new RiskCategory
      riskCategory.setUuid(Utils.generateUuid());
    }
    // Save the RiskCategory and return the DTO
    riskCategory = riskCategoryRepository.save(riskCategory);
    return riskCategoryMapper.toDto(riskCategory);
  }

  /**
   * Finds all RiskCategories based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link RiskCategoryDto}.
   */
  @Override
  public Page<RiskCategoryDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return riskCategoryRepository
      .findAll(createSpecification(RiskCategory.class, searchParams), pageable)
      .map(riskCategoryMapper::toDto);
  }

  /**
   * Finds a RiskCategory by its UUID.
   *
   * @param uuid the UUID of the RiskCategory to find.
   * @return the {@link RiskCategoryDto} if found.
   * @throws ValidationException if the RiskCategory is not found.
   */
  @Override
  public RiskCategoryDto findById(UUID uuid) {
    return riskCategoryRepository
      .findByUuid(uuid)
      .map(riskCategoryMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "RiskCategory  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a RiskCategory by its UUID.
   *
   * @param uuid the UUID of the RiskCategory to delete.
   * @throws ValidationException if the RiskCategory cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      riskCategoryRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete RiskCategory with uuid " + uuid
      );
    }
  }
}
