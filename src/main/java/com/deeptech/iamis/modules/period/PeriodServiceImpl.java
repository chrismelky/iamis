package com.deeptech.iamis.modules.period;

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
 * Service implementation for managing Periods. Provides CRUD operations for Periods
 * This service uses {@link PeriodRepository} for database operations and {@link PeriodMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for Periods.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PeriodServiceImpl
  extends SearchService<Period>
  implements PeriodService {

  private final PeriodRepository periodRepository;

  private final PeriodMapper periodMapper;

  /**
   * Creates or updates a Period based on the presence of a UUID in the DTO.
   *
   * @param periodDto the {@link PeriodDto} containing Period data.
   * @return the saved {@link PeriodDto}.
   * @throws EntityNotFoundException if the Period is not found for update or there is validation failure.
   */
  @Override
  public PeriodDto save(PeriodDto periodDto) {
    // Convert the DTO to an entity object
    Period period = periodMapper.toEntity(periodDto);

    // If the Period has a UUID, fetch the existing Period and perform an update
    if (periodDto.getUuid() != null) {
      period =
        periodRepository
          .findByUuid(periodDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "period with uuid" + periodDto.getUuid() + " not found"
            )
          );
      // Partially update the existing Period with new data from the DTO
      period = periodMapper.partialUpdate(periodDto, period);
    } else {
      // Set a new UUID for a new Period
      period.setUuid(Utils.generateUuid());
    }
    // Save the Period and return the DTO
    period = periodRepository.save(period);
    return periodMapper.toDto(period);
  }

  /**
   * Finds all Periods based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link PeriodDto}.
   */
  @Override
  public Page<PeriodDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return periodRepository
      .findAll(createSpecification(Period.class, searchParams), pageable)
      .map(periodMapper::toDto);
  }

  /**
   * Finds a Period by its UUID.
   *
   * @param uuid the UUID of the Period to find.
   * @return the {@link PeriodDto} if found.
   * @throws ValidationException if the Period is not found.
   */
  @Override
  public PeriodDto findById(UUID uuid) {
    return periodRepository
      .findByUuid(uuid)
      .map(periodMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException("Period  with uuid " + uuid + " not found")
      );
  }

  /**
   * Deletes a Period by its UUID.
   *
   * @param uuid the UUID of the Period to delete.
   * @throws ValidationException if the Period cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      periodRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException("Cannot delete Period with uuid " + uuid);
    }
  }
}
