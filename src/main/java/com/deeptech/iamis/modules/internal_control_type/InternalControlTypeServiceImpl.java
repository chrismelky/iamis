package com.deeptech.iamis.modules.internal_control_type;

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
 * Service implementation for managing InternalControlTypes. Provides CRUD operations for InternalControlTypes
 * This service uses {@link InternalControlTypeRepository} for database operations and {@link InternalControlTypeMapper}
 * to convert between entity and DTO objects.
 * <p>
 * It also extends {@link SearchService} to enable advanced search capabilities for InternalControlTypes.
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InternalControlTypeServiceImpl
  extends SearchService<InternalControlType>
  implements InternalControlTypeService {

  private final InternalControlTypeRepository internalControlTypeRepository;

  private final InternalControlTypeMapper internalControlTypeMapper;

  /**
   * Creates or updates a InternalControlType based on the presence of a UUID in the DTO.
   *
   * @param internalControlTypeDto the {@link InternalControlTypeDto} containing InternalControlType data.
   * @return the saved {@link InternalControlTypeDto}.
   * @throws EntityNotFoundException if the InternalControlType is not found for update or there is validation failure.
   */
  @Override
  public InternalControlTypeDto save(
    InternalControlTypeDto internalControlTypeDto
  ) {
    // Convert the DTO to an entity object
    InternalControlType internalControlType =
      internalControlTypeMapper.toEntity(internalControlTypeDto);

    // If the InternalControlType has a UUID, fetch the existing InternalControlType and perform an update
    if (internalControlTypeDto.getUuid() != null) {
      internalControlType =
        internalControlTypeRepository
          .findByUuid(internalControlTypeDto.getUuid())
          .orElseThrow(() ->
            new EntityNotFoundException(
              "internalControlType with uuid" +
              internalControlTypeDto.getUuid() +
              " not found"
            )
          );
      // Partially update the existing InternalControlType with new data from the DTO
      internalControlType =
        internalControlTypeMapper.partialUpdate(
          internalControlTypeDto,
          internalControlType
        );
    } else {
      // Set a new UUID for a new InternalControlType
      internalControlType.setUuid(Utils.generateUuid());
    }
    // Save the InternalControlType and return the DTO
    internalControlType =
      internalControlTypeRepository.save(internalControlType);
    return internalControlTypeMapper.toDto(internalControlType);
  }

  /**
   * Finds all InternalControlTypes based on pagination and search criteria.
   *
   * @param pageable the pagination information.
   * @param searchParams   a map of search criteria.
   * @return a paginated list of {@link InternalControlTypeDto}.
   */
  @Override
  public Page<InternalControlTypeDto> findAll(
    Pageable pageable,
    Map<String, Object> searchParams
  ) {
    return internalControlTypeRepository
      .findAll(
        createSpecification(InternalControlType.class, searchParams),
        pageable
      )
      .map(internalControlTypeMapper::toDto);
  }

  /**
   * Finds a InternalControlType by its UUID.
   *
   * @param uuid the UUID of the InternalControlType to find.
   * @return the {@link InternalControlTypeDto} if found.
   * @throws ValidationException if the InternalControlType is not found.
   */
  @Override
  public InternalControlTypeDto findById(UUID uuid) {
    return internalControlTypeRepository
      .findByUuid(uuid)
      .map(internalControlTypeMapper::toDto)
      .orElseThrow(() ->
        new EntityNotFoundException(
          "InternalControlType  with uuid " + uuid + " not found"
        )
      );
  }

  /**
   * Deletes a InternalControlType by its UUID.
   *
   * @param uuid the UUID of the InternalControlType to delete.
   * @throws ValidationException if the InternalControlType cannot be deleted.
   */
  @Override
  public void delete(UUID uuid) {
    try {
      internalControlTypeRepository.deleteByUuid(uuid);
    } catch (Exception e) {
      throw new ValidationException(
        "Cannot delete InternalControlType with uuid " + uuid
      );
    }
  }
}
