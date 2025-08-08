package com.deeptech.iamis.modules.position;

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
 * Service implementation for managing Positions. Provides CRUD operations for Positions
 * This service uses {@link PositionRepository} for database operations and {@link PositionMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for Positions.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PositionServiceImpl
  extends SearchService<Position>
  implements PositionService {

  private final PositionRepository positionRepository;

  private final PositionMapper positionMapper;

  /**
   * Creates or updates a Position based on the presence of a UUID in the DTO.
   *
   * @param positionDto the {@link PositionDto} containing Position data.
   * @return the saved {@link PositionDto}.
   * @throws EntityNotFoundException if the Position is not found for update or there is validation failure.
   */
  @Override
  public PositionDto save(PositionDto positionDto) {
    // Convert the DTO to an entity object
    Position position = positionMapper.toEntity(positionDto);

    // If the Position has a UUID, fetch the existing Position and perform an update
    if (positionDto.getUuid() != null) {
      position =
        positionRepository
          .findByUuid(positionDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "position with uuid" + positionDto.getUuid() + " not found"
            )
          );
      // Partially update the existing Position with new data from the DTO
      position = positionMapper.partialUpdate(positionDto, position);
    } else {
      // Set a new UUID for a new Position
      position.setUuid(Utils.generateUuid());
    }
    // Save the Position and return the DTO
    position = positionRepository.save(position);
    return positionMapper.toDto(position);
  }

  /**
   * Finds all Positions based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link PositionDto}.
   */
  @Override
  public Page<PositionDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return positionRepository
      .findAll(createSpecification(Position.class, searchParams), pageable)
      .map(positionMapper::toDto);
  }

  /**
   * Finds a Position by its UUID.
   *
   * @param uuid the UUID of the Position to find.
   * @return the {@link PositionDto} if found.
   * @throws ValidationException if the Position is not found.
   */
  @Override
  public PositionDto findById(UUID uuid) {
    return positionRepository
      .findByUuid(uuid)
      .map(positionMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "Position  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a Position by its UUID.
   *
   * @param uuid the UUID of the Position to delete.
   * @throws ValidationException if the Position cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      positionRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException("Cannot delete Position with uuid " + uuid);
    }
  }
}
