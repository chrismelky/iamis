package com.deeptech.iamis.modules.financial_year;

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
 * Service implementation for managing FinancialYears. Provides CRUD operations for FinancialYears
 * This service uses {@link FinancialYearRepository} for database operations and {@link FinancialYearMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for FinancialYears.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FinancialYearServiceImpl
  extends SearchService<FinancialYear>
  implements FinancialYearService {

  private final FinancialYearRepository financialYearRepository;

  private final FinancialYearMapper financialYearMapper;

  /**
   * Creates or updates a FinancialYear based on the presence of a UUID in the DTO.
   *
   * @param financialYearDto the {@link FinancialYearDto} containing FinancialYear data.
   * @return the saved {@link FinancialYearDto}.
   * @throws EntityNotFoundException if the FinancialYear is not found for update or there is validation failure.
   */
  @Override
  public FinancialYearDto save(FinancialYearDto financialYearDto) {
    // Convert the DTO to an entity object
    FinancialYear financialYear = financialYearMapper.toEntity(
      financialYearDto
    );

    // If the FinancialYear has a UUID, fetch the existing FinancialYear and perform an update
    if (financialYearDto.getUuid() != null) {
      financialYear =
        financialYearRepository
          .findByUuid(financialYearDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "financialYear with uuid" +
              financialYearDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing FinancialYear with new data from the DTO
      financialYear =
        financialYearMapper.partialUpdate(financialYearDto, financialYear);
    } else {
      // Set a new UUID for a new FinancialYear
      financialYear.setUuid(Utils.generateUuid());
    }
    // Save the FinancialYear and return the DTO
    financialYear = financialYearRepository.save(financialYear);
    return financialYearMapper.toDto(financialYear);
  }

  /**
   * Finds all FinancialYears based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link FinancialYearDto}.
   */
  @Override
  public Page<FinancialYearDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return financialYearRepository
      .findAll(createSpecification(FinancialYear.class, searchParams), pageable)
      .map(financialYearMapper::toDto);
  }

  /**
   * Finds a FinancialYear by its UUID.
   *
   * @param uuid the UUID of the FinancialYear to find.
   * @return the {@link FinancialYearDto} if found.
   * @throws ValidationException if the FinancialYear is not found.
   */
  @Override
  public FinancialYearDto findById(UUID uuid) {
    return financialYearRepository
      .findByUuid(uuid)
      .map(financialYearMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "FinancialYear  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a FinancialYear by its UUID.
   *
   * @param uuid the UUID of the FinancialYear to delete.
   * @throws ValidationException if the FinancialYear cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      financialYearRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete FinancialYear with uuid " + uuid
      );
    }
  }
}
